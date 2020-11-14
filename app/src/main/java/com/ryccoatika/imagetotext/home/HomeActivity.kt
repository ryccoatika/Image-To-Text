package com.ryccoatika.imagetotext.home

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import com.ryccoatika.imagetotext.core.ui.HomeAdapter
import com.ryccoatika.imagetotext.textscanneddetail.TextScannedDetailActivity
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_error.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class HomeActivity: AppCompatActivity(), HomeView {

    private var mCropImageUri: Uri? = null
    private val homeViewModel: HomeViewModel by viewModel { parametersOf(this as HomeView) }
    private val homeAdapter = HomeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        if ( intent.action == Intent.ACTION_SEND ) {
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
        val itemTouchHelper = ItemTouchHelper(object: ItemSwipeShowMenu(applicationContext) {
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
            if (CropImage.isExplicitCameraPermissionRequired(applicationContext)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CAMERA),
                        CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
                }
            } else {
                CropImage.startPickImageActivity(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getAllTextScanned()
    }

    private fun imageToText(uri: Uri) {
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val image = FirebaseVisionImage.fromFilePath(applicationContext, uri)

        detector.processImage(image)
            .addOnCompleteListener { firebaseVisionText ->
                firebaseVisionText.result?.let { textResult ->
                    if (textResult.text.isEmpty()) {
                        showToast(getString(R.string.text_not_detected))
                    } else {
                        val textScanned = TextScanned(
                            dateTime = System.currentTimeMillis(),
                            textResult.text
                        )
                        homeViewModel.insertTextScanned(textScanned)
                    }
                }
            }
            .addOnFailureListener { error ->
                error.printStackTrace()
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // when request permission is PICK IMAGE
            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE -> {
                if (mCropImageUri != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCropActivity(mCropImageUri as Uri)
                }
            }
            // when image is from camera capture
            CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.startPickImageActivity(this)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // when image
            CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) { // when result of the activity is OK
                    val imageUri = CropImage.getPickImageResultUri(applicationContext, data)

                    if (CropImage.isReadExternalStoragePermissionsRequired(applicationContext, imageUri)) {
                        mCropImageUri = imageUri
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE)
                        }
                    } else {
                        startCropActivity(imageUri)
                    }
                }
            }
            // when image cropped
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = CropImage.getActivityResult(data).uri
                    imageToText(uri)
                }
            }
        }
    }

    private fun startCropActivity(uri: Uri) {
        CropImage.activity(uri).start(this)
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