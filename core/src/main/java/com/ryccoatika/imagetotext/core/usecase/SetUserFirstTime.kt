package com.ryccoatika.imagetotext.core.usecase

import com.ryccoatika.imagetotext.core.repository.TextScannedRepository
import com.ryccoatika.imagetotext.core.utils.AppCoroutineDispatchers
import com.ryccoatika.imagetotext.core.utils.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetUserFirstTime @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val textScannedRepository: TextScannedRepository
) : Interactor<SetUserFirstTime.Params>() {
    override suspend fun doWork(params: Params) = withContext(dispatchers.io) {
        textScannedRepository.setUserFirstTime(params.isFirstTime)
    }

    data class Params(
        val isFirstTime: Boolean
    )
}