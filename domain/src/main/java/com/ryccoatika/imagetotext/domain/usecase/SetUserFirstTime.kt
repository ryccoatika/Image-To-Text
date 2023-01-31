package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.utils.AppCoroutineDispatchers
import com.ryccoatika.imagetotext.domain.utils.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetUserFirstTime @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val textScannedRepository: com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
) : Interactor<SetUserFirstTime.Params>() {
    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        textScannedRepository.setUserFirstTime(params.isFirstTime)
    }

    data class Params(
        val isFirstTime: Boolean
    )
}