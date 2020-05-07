package com.ryccoatika.imagetotext.scannedtext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.google.android.material.snackbar.Snackbar
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.db.AppDatabase
import com.ryccoatika.imagetotext.db.TextScannedEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scanned_text.*

class ScannedTextActivity : AppCompatActivity(), View.OnClickListener {

    private var editMode = false
    private lateinit var textScanned: TextScannedEntity
    private val disposable = CompositeDisposable()
    private var database: AppDatabase? = null
    init {
        database = AppDatabase.getInstance(this)
    }

    companion object {
        const val EXTRA_TEXT_SCANNED = "extra_text_scanned"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanned_text)

        intent.getParcelableExtra<TextScannedEntity>(EXTRA_TEXT_SCANNED).also {
            if (it == null) {
                this.finish()
                return
            } else {
                textScanned = it
            }
        }

        // hide save button on the first start
        scanned_text_btn_save.visibility = View.GONE

        scanned_text_edit_content.setText(textScanned.text)

        // toggle for save button
        // when text is changed then the save button will come out
        scanned_text_edit_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != textScanned.text) {
                    scanned_text_btn_save.visibility = View.VISIBLE
                } else {
                    scanned_text_btn_save.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        scanned_text_btn_back.setOnClickListener(this)
        scanned_text_btn_copy.setOnClickListener(this)
        scanned_text_btn_edit.setOnClickListener(this)
        scanned_text_btn_delete.setOnClickListener(this)
        scanned_text_btn_save.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.scanned_text_btn_back -> finish()

            R.id.scanned_text_btn_edit -> {
                if (editMode) {
                    scanned_text_edit_content.isEnabled = false
                    editMode = false
                } else {
                    scanned_text_edit_content.isEnabled = true
                    editMode = true
                }
            }

            R.id.scanned_text_btn_copy -> {
                val content = scanned_text_edit_content.text
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Text Scanned", content))
                Snackbar.make(scanned_text_btn_copy, getString(R.string.text_copied), Snackbar.LENGTH_SHORT).show()
            }

            R.id.scanned_text_btn_delete -> {
                database?.let { appDatabase ->
                    appDatabase.textScannedDao().delete(textScanned).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            this.finish()
                        }, { error ->
                            Crashlytics.log("appDatabase.textScannedDao().delete")
                            Crashlytics.logException(error)
                        }).also {
                            disposable.add(it)
                        }
                }
            }

            R.id.scanned_text_btn_save -> {
                textScanned.text = scanned_text_edit_content.text.toString()
                database?.let { appDatabase ->
                    appDatabase.textScannedDao().insert(textScanned).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            Toast.makeText(this, getString(R.string.text_saved), Toast.LENGTH_SHORT).show()
                            scanned_text_edit_content.isEnabled = false
                            scanned_text_btn_save.visibility = View.GONE
                        }, { error ->
                            Crashlytics.log("appDatabase.textScannedDao().insert")
                            Crashlytics.logException(error)
                        }).also {
                            disposable.add(it)
                        }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        disposable.clear()
    }
}