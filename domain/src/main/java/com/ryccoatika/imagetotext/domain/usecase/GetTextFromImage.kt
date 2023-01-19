package com.ryccoatika.imagetotext.domain.usecase

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.repository.TextRecognitionRepository
import com.ryccoatika.imagetotext.domain.utils.ResultInteractor
import javax.inject.Inject


class GetTextFromImage @Inject constructor(
    private val textRecognitionRepository: TextRecognitionRepository
) : ResultInteractor<GetTextFromImage.Params, List<Text.TextBlock>>() {
    override suspend fun doWork(params: Params): List<Text.TextBlock> {
        return textRecognitionRepository.convertImageToText(params.inputImage, params.languageModel)
    }

    data class Params(
        val inputImage: InputImage,
        val languageModel: RecognationLanguageModel
    )
}