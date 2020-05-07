package com.ryccoatika.imagetotext.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.ryccoatika.imagetotext.*
import com.ryccoatika.imagetotext.db.TextScannedEntity
import com.ryccoatika.imagetotext.scannedtext.ScannedTextActivity
import com.theartofdev.edmodo.cropper.CropImage
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import java.util.concurrent.TimeUnit

class HomeActivity: AppCompatActivity(), HomeView, View.OnClickListener {

    private var mCropImageUri: Uri? = null
    private val listAdapter = HomeListAdapter()
    private val presenter: HomePresenter by lazy {
        HomePresenter(
            this,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rv_home.adapter = listAdapter

        val itemTouchHelper = ItemTouchHelper(object: ItemSwipeShowMenu(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                presenter.deleteTextScanned(listAdapter.textScannedList[viewHolder.adapterPosition])
            }
        })
        itemTouchHelper.attachToRecyclerView(rv_home)

        home_edit_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.isEmpty()) {
                    presenter.loadTextScanned()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        val searchObserver = object : Observer<String> {
            override fun onComplete() {
            }
            override fun onSubscribe(d: Disposable) {
            }
            override fun onNext(t: String) {
                presenter.searchTextScanned(t)
            }
            override fun onError(e: Throwable) {
            }
        }

        home_edit_search.textChangeEvents()
            .map { it.text.toString() }
            .filter { it.length > 3 }
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe(searchObserver)

        home_btn_add_image.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadTextScanned()
    }

    private fun imageToText(uri: Uri) {
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer
        val image = FirebaseVisionImage.fromFilePath(this, uri)

        detector.processImage(image)
            .addOnCompleteListener { firebaseVisionText ->
                firebaseVisionText.result?.let {
                    if (it.text.isEmpty()) {
                        Toast.makeText(this, getString(R.string.text_not_detected), Toast.LENGTH_SHORT).show()
                    } else {
                        presenter.insertTextScanned(it.text)
                    }
                }
            }
            .addOnFailureListener { error ->
                Crashlytics.log("detector.processImage.addOnFailureListener")
                Crashlytics.logException(error)
            }
    }

    override fun onShowLoading() {
        pb_home.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        pb_home.visibility = View.GONE
    }

    override fun onGetTextScannedResponse(results: List<TextScannedEntity>) {
        listAdapter.textScannedList = results
    }

    override fun onSearchTextScannedResponse(results: List<TextScannedEntity>) {
        listAdapter.textScannedList = results
    }

    override fun onInsertCompleted(result: TextScannedEntity) {
        presenter.loadTextScanned()
        val intentScannedText = Intent(this, ScannedTextActivity::class.java)
        intentScannedText.putExtra(ScannedTextActivity.EXTRA_TEXT_SCANNED, result)
        startActivity(intentScannedText)
    }

    override fun onDeleteCompleted() {
        presenter.loadTextScanned()
    }

    override fun onGetTextScannedFailure(error: Throwable) {
        Crashlytics.log("onGetTextScannedFailure")
        Crashlytics.logException(error)
    }

    override fun onSearchTextScannedFailure(error: Throwable) {
        Crashlytics.log("onSearchTextScannedFailure")
        Crashlytics.logException(error)
    }

    override fun onInsertFailure(error: Throwable) {
        Crashlytics.log("onInsertFailure")
        Crashlytics.logException(error)
    }

    override fun onDeleteFailure(error: Throwable) {
        Crashlytics.log("onDeleteFailure")
        Crashlytics.logException(error)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.home_btn_add_image -> {
                // start pick image activity
                if (CropImage.isExplicitCameraPermissionRequired(this)) {
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) { // when result of the activity is OK
                    val imageUri = CropImage.getPickImageResultUri(this, data)

                    if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // when request permission is PICK IMAGE
            CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE -> {
                if (mCropImageUri != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCropActivity(mCropImageUri as Uri)
                }
            }
            CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CropImage.startPickImageActivity(this)
                }
            }
        }
    }

    private fun startCropActivity(uri: Uri) {
        CropImage.activity(uri).start(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}