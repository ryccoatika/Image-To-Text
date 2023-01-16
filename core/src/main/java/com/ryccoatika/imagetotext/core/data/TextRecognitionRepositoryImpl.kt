package com.ryccoatika.imagetotext.core.data

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.repository.TextRecognitionRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextRecognitionRepositoryImpl @Inject constructor() : TextRecognitionRepository {
    override suspend fun convertImageToText(
        inputImage: InputImage,
        languageModel: RecognationLanguageModel
    ): Text = suspendCoroutine { continuation ->
        val recognizer = when (languageModel) {
            RecognationLanguageModel.LATIN -> TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            else -> {
                continuation.resume(Text("", emptyList<Any?>()))
                return@suspendCoroutine
            }
        }

        recognizer.process(inputImage)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result)
                } else {
                    task.exception?.printStackTrace()
                    continuation.resume(Text("", emptyList<Any?>()))
                }
            }
    }
}