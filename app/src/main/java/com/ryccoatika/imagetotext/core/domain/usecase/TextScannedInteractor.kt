package com.ryccoatika.imagetotext.core.domain.usecase

import com.ryccoatika.imagetotext.core.data.Resource
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import com.ryccoatika.imagetotext.core.domain.repository.ITextScannedRepository
import io.reactivex.Completable
import io.reactivex.Flowable

class TextScannedInteractor(
    private val textScannedRepository: ITextScannedRepository
): TextScannedUseCase {
    override fun insertTextScanned(textScanned: TextScanned): Completable =
        textScannedRepository.insertTextScanned(textScanned)

    override fun deleteTextScanned(textScanned: TextScanned): Completable =
        textScannedRepository.deleteTextScanned(textScanned)

    override fun getAllTextScanned(): Flowable<Resource<List<TextScanned>>> =
        textScannedRepository.getAllTextScanned()

    override fun searchTextScanned(query: String): Flowable<Resource<List<TextScanned>>> =
        textScannedRepository.searchTextScanned(query)
}