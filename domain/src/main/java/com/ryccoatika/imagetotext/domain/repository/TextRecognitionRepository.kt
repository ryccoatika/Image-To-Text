package com.ryccoatika.imagetotext.domain.repository

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel

interface TextRecognitionRepository {
    suspend fun convertImageToText(inputImage: InputImage, languageModel: RecognationLanguageModel): List<Text.Element>
}