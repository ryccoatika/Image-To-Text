package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import com.ryccoatika.imagetotext.domain.utils.AppCoroutineDispatchers
import com.ryccoatika.imagetotext.domain.utils.Interactor
import javax.inject.Inject
import kotlinx.coroutines.withContext

class SetUserFirstTime @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val textScannedRepository: TextScannedRepository,
) : Interactor<SetUserFirstTime.Params>() {
    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        textScannedRepository.setUserFirstTime(params.isFirstTime)
    }

    data class Params(
        val isFirstTime: Boolean,
    )
}
