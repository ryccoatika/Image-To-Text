package com.ryccoatika.imagetotext.core.usecase

import com.ryccoatika.imagetotext.core.repository.TextScannedRepository
import com.ryccoatika.imagetotext.core.utils.ResultInteractor
import javax.inject.Inject

class GetIsUserFirstTime @Inject constructor(
    private val textScannedRepository: TextScannedRepository
) : ResultInteractor<Unit, Boolean>() {
    override suspend fun doWork(params: Unit): Boolean {
        return textScannedRepository.getIsUserFirstTime()
    }
}