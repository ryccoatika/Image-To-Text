package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.utils.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTextScanned @Inject constructor(
    private val textScannedRepository: com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
) : SubjectInteractor<ObserveTextScanned.Params, List<TextScanned>>() {
    override fun createObservable(params: Params): Flow<List<com.ryccoatika.imagetotext.domain.model.TextScanned>> {
        return textScannedRepository.getAllTextScanned(params.query)
    }

    data class Params(
        val query: String? = null
    )
}