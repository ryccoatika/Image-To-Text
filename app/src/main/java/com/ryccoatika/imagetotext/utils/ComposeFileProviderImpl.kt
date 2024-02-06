package com.ryccoatika.imagetotext.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.ryccoatika.imagetotext.R
import com.ryccoatika.imagetotext.domain.utils.ComposeFileProvider
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComposeFileProviderImpl @Inject constructor() : ComposeFileProvider, FileProvider(R.xml.filepaths) {
    override fun getImageUri(context: Context): Uri {
        val directory = File(context.cacheDir, "images")
        directory.mkdirs()
        val file = File.createTempFile(
            "image_to_text_",
            ".jpg",
            directory,
        )
        val authority = context.packageName + ".fileprovider"
        return getUriForFile(
            context,
            authority,
            file,
        )
    }
}
