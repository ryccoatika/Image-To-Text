package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import com.ryccoatika.imagetotext.domain.utils.ResultInteractor
import javax.inject.Inject

class GetIsUserFirstTime @Inject constructor(
    private val textScannedRepository: TextScannedRepository,
) : ResultInteractor<Unit, Boolean>() {
    override suspend fun doWork(params: Unit): Boolean {
        return textScannedRepository.getIsUserFirstTime()
    }
}
