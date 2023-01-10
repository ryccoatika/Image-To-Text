package com.ryccoatika.imagetotext.core.usecase

import com.ryccoatika.imagetotext.core.model.TextScanned
import com.ryccoatika.imagetotext.core.repository.TextScannedRepository
import com.ryccoatika.imagetotext.core.utils.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTextScanned @Inject constructor(
    private val textScannedRepository: TextScannedRepository
) : SubjectInteractor<ObserveTextScanned.Params, List<TextScanned>>() {
    override fun createObservable(params: Params): Flow<List<TextScanned>> {
        return textScannedRepository.getAllTextScanned(params.query)
    }

    data class Params(
        val query: String? = null
    )
}