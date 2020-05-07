package com.ryccoatika.imagetotext.home

import com.ryccoatika.imagetotext.db.TextScannedEntity

interface HomeView {
    fun onShowLoading()
    fun onHideLoading()

    fun onGetTextScannedResponse(results: List<TextScannedEntity>)
    fun onSearchTextScannedResponse(results: List<TextScannedEntity>)
    fun onInsertCompleted(result: TextScannedEntity)
    fun onDeleteCompleted()

    fun onGetTextScannedFailure(error: Throwable)
    fun onSearchTextScannedFailure(error: Throwable)
    fun onInsertFailure(error: Throwable)
    fun onDeleteFailure(error: Throwable)
}