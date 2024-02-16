package com.ryccoatika.imagetotext.ui.utils

import android.content.Context
import android.content.Intent

fun Context.share(text: String) {
    Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }.run {
        startActivity(Intent.createChooser(this, null))
    }
}
