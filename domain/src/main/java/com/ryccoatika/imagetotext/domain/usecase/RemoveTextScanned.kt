package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import com.ryccoatika.imagetotext.domain.utils.AppCoroutineDispatchers
import com.ryccoatika.imagetotext.domain.utils.Interactor
import javax.inject.Inject
import kotlinx.coroutines.withContext

class RemoveTextScanned @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val textScannedRepository: TextScannedRepository,
) : Interactor<RemoveTextScanned.Params>() {

    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        textScannedRepository.removeTextScanned(params.textScanned)
    }

    data class Params(
        val textScanned: com.ryccoatika.imagetotext.domain.model.TextScanned,
    )
}
