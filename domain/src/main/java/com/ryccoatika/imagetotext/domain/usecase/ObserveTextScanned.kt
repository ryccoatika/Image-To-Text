package com.ryccoatika.imagetotext.domain.usecase

import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.repository.TextScannedRepository
import com.ryccoatika.imagetotext.domain.utils.SubjectInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveTextScanned @Inject constructor(
    private val textScannedRepository: TextScannedRepository,
) : SubjectInteractor<ObserveTextScanned.Params, List<TextScanned>>() {
    override fun createObservable(params: Params): Flow<List<TextScanned>> {
        return textScannedRepository.getAllTextScanned(params.query)
    }

    data class Params(
        val query: String? = null,
    )
}
