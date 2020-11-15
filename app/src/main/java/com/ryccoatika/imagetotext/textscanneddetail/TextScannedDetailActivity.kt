package com.ryccoatika.imagetotext.textscanneddetail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import kotlinx.android.synthetic.main.activity_text_scanned_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TextScannedDetailActivity : AppCompatActivity(), TextScannedDetailView, View.OnClickListener {

    private val textScannedViewModel: TextScannedDetailViewModel by viewModel { parametersOf(this) }
    private var uiMode = UI_MODE_VIEW
    private lateinit var textScanned: TextScanned

    companion object {
        const val EXTRA_TEXT_SCANNED = "extra_text_scanned"
        private const val UI_MODE_EDIT = 1
        private const val UI_MODE_VIEW = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_scanned_detail)

        updateUI(uiMode)

        val textScanned = intent.getParcelableExtra<TextScanned>(EXTRA_TEXT_SCANNED)

        if (textScanned != null) {
            this.textScanned = textScanned
            edit_content.setText(textScanned.text)
        }

        btn_back.setOnClickListener(this)
        btn_copy.setOnClickListener(this)
        btn_edit.setOnClickListener(this)
        btn_delete.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        btn_share.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_back -> finish()

            R.id.btn_edit -> {
                uiMode = if (uiMode == UI_MODE_EDIT)
                    UI_MODE_VIEW
                else
                    UI_MODE_EDIT
                updateUI(uiMode)
            }

            R.id.btn_copy -> {
                val content = edit_content.text
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboard.setPrimaryClip(ClipData.newPlainText("Text Scanned", content))
                showToast(getString(R.string.text_copied))
            }

            R.id.btn_delete -> {
                textScannedViewModel.deleteTextScanned(textScanned)
            }

            R.id.btn_save -> {
                textScanned.text = edit_content.text.toString()
                textScannedViewModel.updateTextScanned(textScanned)
            }

            R.id.btn_share -> {
                val content = edit_content.text.toString()
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, content)
                }
                startActivity(Intent.createChooser(intent, getString(R.string.text_open_with)))
            }
        }
    }

    override fun onDeleteSuccess() {
        finish()
    }

    override fun onUpdateSuccess() {
        showToast(getString(R.string.text_saved))
        uiMode = UI_MODE_VIEW
        updateUI(uiMode)
    }

    override fun onError() {
        showToast(getString(R.string.text_went_wrong))
    }

    private fun updateUI(mode: Int) {
        when (mode) {
            UI_MODE_EDIT -> {
                edit_content.isEnabled = true
                btn_save.visibility = View.VISIBLE
                btn_share.visibility = View.GONE
            }
            UI_MODE_VIEW -> {
                edit_content.isEnabled = false
                btn_save.visibility = View.GONE
                btn_share.visibility = View.VISIBLE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}