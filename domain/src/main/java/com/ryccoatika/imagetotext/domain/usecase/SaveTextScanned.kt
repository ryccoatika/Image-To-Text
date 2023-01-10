package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.utils.AppCoroutineDispatchers
import com.ryccoatika.imagetotext.domain.utils.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SaveTextScanned @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val textScannedRepository: com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
) : Interactor<SaveTextScanned.Params>() {
    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        textScannedRepository.saveTextScanned(params.textScanned)
    }

    data class Params(
        val textScanned: com.ryccoatika.imagetotext.domain.model.TextScanned
    )
}