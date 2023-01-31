package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import com.ryccoatika.imagetotext.domain.utils.ResultInteractor
import javax.inject.Inject

class GetTextScanned @Inject constructor(
    private val textScannedRepository: TextScannedRepository
) : ResultInteractor<GetTextScanned.Params, TextScanned>() {
    override suspend fun doWork(params: Params): TextScanned = textScannedRepository.getTextScanned(params.id)

    data class Params(
        val id: Long
    )
}