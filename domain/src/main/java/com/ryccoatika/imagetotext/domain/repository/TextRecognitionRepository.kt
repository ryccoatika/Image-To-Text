package com.ryccoatika.imagetotext.domain.repository

import com.google.mlkit.vision.common.InputImage
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.model.TextRecognized

interface TextRecognitionRepository {
    suspend fun convertImageToText(inputImage: InputImage, languageModel: RecognationLanguageModel): TextRecognized
}