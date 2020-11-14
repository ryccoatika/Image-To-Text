package com.ryccoatika.imagetotext.home

import com.ryccoatika.imagetotext.core.domain.model.TextScanned

interface HomeView {
    fun onLoadListTextScannedSuccess(listTextScanned: List<TextScanned>)
    fun onLoadListTextScannedError(message: String)
    fun onLoadListTextScannedLoading()
    fun onLoadListTextScannedEmpty()
    fun onError()
    fun onInsertSuccess(textScanned: TextScanned)
    fun onDeleteSuccess()
}