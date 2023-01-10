package com.ryccoatika.imagetotext.core.usecase

import com.ryccoatika.imagetotext.core.model.TextScanned
import com.ryccoatika.imagetotext.core.repository.TextScannedRepository
import com.ryccoatika.imagetotext.core.utils.AppCoroutineDispatchers
import com.ryccoatika.imagetotext.core.utils.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddTextScanned @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val textScannedRepository: TextScannedRepository
) : Interactor<AddTextScanned.Params>() {
    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        textScannedRepository.saveTextScanned(params.textScanned)
    }

    data class Params(
        val textScanned: TextScanned
    )
}