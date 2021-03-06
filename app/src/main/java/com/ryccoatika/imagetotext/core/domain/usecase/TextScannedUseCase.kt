package com.ryccoatika.imagetotext.core.domain.usecase

import com.ryccoatika.imagetotext.core.data.Resource
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import io.reactivex.Completable
import io.reactivex.Flowable

interface TextScannedUseCase {
    fun insertTextScanned(textScanned: TextScanned): Completable

    fun deleteTextScanned(textScanned: TextScanned): Completable

    fun getAllTextScanned(): Flowable<Resource<List<TextScanned>>>

    fun searchTextScanned(query: String): Flowable<Resource<List<TextScanned>>>
}