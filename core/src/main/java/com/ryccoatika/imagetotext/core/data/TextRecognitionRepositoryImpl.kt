package com.ryccoatika.imagetotext.core.data

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
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
    ): List<Text.Element> = suspendCoroutine { continuation ->
        val recognizer = when (languageModel) {
            RecognationLanguageModel.LATIN -> TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            RecognationLanguageModel.CHINESE -> TextRecognition.getClient(
                ChineseTextRecognizerOptions.Builder().build()
            )
            RecognationLanguageModel.DEVANAGARI -> TextRecognition.getClient(
                DevanagariTextRecognizerOptions.Builder().build()
            )
            RecognationLanguageModel.JAPANESE -> TextRecognition.getClient(
                JapaneseTextRecognizerOptions.Builder().build()
            )
            RecognationLanguageModel.KOREAN -> TextRecognition.getClient(
                KoreanTextRecognizerOptions.Builder().build()
            )
            else -> {
                continuation.resume(emptyList())
                return@suspendCoroutine
            }
        }

        recognizer.process(inputImage)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val elements =
                        task.result.textBlocks.fold(emptyList<Text.Element>()) { acc1, textBlock ->
                            acc1 + textBlock.lines.fold(emptyList()) { acc2, line ->
                                acc2 + line.elements.fold(emptyList()) { acc3, element ->
                                    acc3 + element
                                }
                            }
                        }
                    continuation.resume(elements)
                } else {
                    task.exception?.printStackTrace()
                    continuation.resume(emptyList())
                }
            }
    }
}