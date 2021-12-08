package com.ryccoatika.imagetotext.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.options
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import com.ryccoatika.imagetotext.core.ui.HomeAdapter
import com.ryccoatika.imagetotext.core.utils.ImageToText
import com.ryccoatika.imagetotext.textscanneddetail.TextScannedDetailActivity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_error.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class HomeActivity : AppCompatActivity(), HomeView {

    private val homeViewModel: HomeViewModel by viewModel { parametersOf(this as HomeView) }
    private val homeAdapter = HomeAdapter()

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent

            val imageToText = ImageToText.Builder(this)
                .addOnCompleteListener { textResult ->
                    if (textResult != null) {
                        if (textResult.isEmpty()) {
                            showToast(getString(R.string.text_not_detected))
                        } else {
                            val textScanned = TextScanned(
                                dateTime = System.currentTimeMillis(),
                                textResult
                            )
                            homeViewModel.insertTextScanned(textScanned)
                        }
                    }
                }
                .addOnFailureListener { error ->
                    error.printStackTrace()
                }
                .build()
            uriContent?.let { imageToText.recognize(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if (intent.action == Intent.ACTION_SEND) {
            val uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
            uri?.let { startCropActivity(it) }
        }

        with(rv_home) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = homeAdapter
        }

        homeAdapter.setOnItemClickListener { textScanned ->
            moveToDetailActivity(textScanned)
        }

        homeAdapter.setOnItemLongClickListener { textScanned ->
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Scanned Text", textScanned.text))
            showToast(getString(R.string.text_copied))
            true
        }

        // implement swipe to delete on recycleview item
        val itemTouchHelper = ItemTouchHelper(object : ItemSwipeShowMenu(applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                homeViewModel.deleteTextScanned(homeAdapter.getTextScanned(viewHolder.adapterPosition))
            }
        })
        itemTouchHelper.attachToRecyclerView(rv_home)

        home_edit_search.textChangeEvents()
            .map { it.text.toString() }
            .skip(1)
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: String) {
                    if (t.isEmpty())
                        homeViewModel.getAllTextScanned()
                    else
                        homeViewModel.searchTextScanned(t)
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })

        fab_add_image.setOnClickListener {
            cropImage.launch(options {
                setImageSource(includeCamera = true, includeGallery = true)

            })
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getAllTextScanned()
    }


    private fun startCropActivity(uri: Uri) {
        cropImage.launch(options(uri))
    }

    override fun onLoadListTextScannedSuccess(listTextScanned: List<TextScanned>) {
        view_loading.visibility = View.GONE
        view_empty.visibility = View.GONE
        homeAdapter.setListTextScanned(listTextScanned)
        Log.d("190401", "onSuccess")
    }

    override fun onLoadListTextScannedError(message: String) {
        view_loading.visibility = View.GONE
        view_error.visibility = View.VISIBLE
        view_empty.visibility = View.GONE
        tv_error.text = message
    }

    override fun onLoadListTextScannedLoading() {
        view_loading.visibility = View.VISIBLE
        view_error.visibility = View.GONE
        view_empty.visibility = View.GONE
    }

    override fun onLoadListTextScannedEmpty() {
        homeAdapter.setListTextScanned(listOf())
        view_loading.visibility = View.GONE
        view_error.visibility = View.GONE
        view_empty.visibility = View.VISIBLE
    }

    override fun onError() {
        showToast(getString(R.string.text_went_wrong))
    }

    override fun onInsertSuccess(textScanned: TextScanned) {
        moveToDetailActivity(textScanned)
    }

    override fun onDeleteSuccess() {
        homeViewModel.getAllTextScanned()
    }

    private fun moveToDetailActivity(textScanned: TextScanned) {
        val intent = Intent(this, TextScannedDetailActivity::class.java)
        intent.putExtra(TextScannedDetailActivity.EXTRA_TEXT_SCANNED, textScanned)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}