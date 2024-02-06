package com.ryccoatika.imagetotext.domain.usecase

import com.google.mlkit.vision.common.InputImage
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.repository.TextRecognitionRepository
import com.ryccoatika.imagetotext.domain.utils.ResultInteractor
import javax.inject.Inject

class GetTextFromImage @Inject constructor(
    private val textRecognitionRepository: TextRecognitionRepository,
) : ResultInteractor<GetTextFromImage.Params, TextRecognized>() {
    override suspend fun doWork(params: Params): TextRecognized {
        return textRecognitionRepository.convertImageToText(params.inputImage, params.languageModel)
    }

    data class Params(
        val inputImage: InputImage,
        val languageModel: RecognationLanguageModel,
    )
}
