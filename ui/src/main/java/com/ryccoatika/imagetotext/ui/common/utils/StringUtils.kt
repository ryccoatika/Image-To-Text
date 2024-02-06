package com.ryccoatika.imagetotext.ui.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.ContextCompat

fun String.copyToClipboard(context: Context) {
    val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
    clipboard?.setPrimaryClip(ClipData.newPlainText("", this))
}
