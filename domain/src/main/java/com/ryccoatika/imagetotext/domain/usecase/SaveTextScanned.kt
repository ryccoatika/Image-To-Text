package com.ryccoatika.imagetotext.domain.usecase

import android.net.Uri
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.utils.ResultInteractor
import javax.inject.Inject

class SaveTextScanned @Inject constructor(
    private val textScannedRepository: com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
) : ResultInteractor<SaveTextScanned.Params, TextScanned>() {

    override suspend fun doWork(params: Params): TextScanned = textScannedRepository.saveTextScanned(
        imageUri = params.imageUri,
        textRecognized = params.textRecognized,
        text = params.text
    )

    data class Params(
        val imageUri: Uri,
        val textRecognized: TextRecognized,
        val text: String
    )
}