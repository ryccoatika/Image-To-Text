package com.ryccoatika.imagetotext.domain.utils

import android.content.Context
import android.net.Uri

interface ComposeFileProvider {
    fun getImageUri(context: Context): Uri
}
