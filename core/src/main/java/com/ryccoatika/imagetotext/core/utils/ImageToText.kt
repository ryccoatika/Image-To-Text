package com.ryccoatika.imagetotext.core.utils

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.lang.Exception

class ImageToText private constructor(builder: Builder){
    private val context: Context = builder.context
    private val onCompleteListener: ((String?) -> Unit)? = builder.onCompleteListener
    private val onFailureListener: ((Exception) -> Unit)? = builder.onFailureListener
    private val textRecognizer: TextRecognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    class Builder(val context: Context) {
        var onCompleteListener: ((String?) -> Unit)? = null
        var onFailureListener: ((Exception) -> Unit)? = null

        fun addOnCompleteListener(onCompleteListener: (String?) -> Unit): Builder {
            this.onCompleteListener = onCompleteListener
            return this
        }

        fun addOnFailureListener(onFailureListener: (Exception) -> Unit): Builder {
            this.onFailureListener = onFailureListener
            return this
        }

        fun build(): ImageToText = ImageToText(this)
    }

    fun recognize(uri: Uri) {
        val image = InputImage.fromFilePath(context, uri)
        textRecognizer.process(image)
            .addOnCompleteListener { onCompleteListener?.invoke(it.result?.text) }
            .addOnFailureListener { onFailureListener?.invoke(it) }
    }
}