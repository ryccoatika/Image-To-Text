package com.ryccoatika.imagetotext.textscanneddetail

import androidx.lifecycle.ViewModel
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import com.ryccoatika.imagetotext.core.domain.usecase.TextScannedUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TextScannedDetailViewModel(
    private val textScannedInteractor: TextScannedUseCase,
    private val textScannedDetailView: TextScannedDetailView
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun deleteTextScanned(textScanned: TextScanned) {
        textScannedInteractor.deleteTextScanned(textScanned).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                textScannedDetailView.onDeleteSuccess()
            }, {
                textScannedDetailView.onError()
            }).let(compositeDisposable::add)
    }

    fun updateTextScanned(textScanned: TextScanned) {
        textScannedInteractor.insertTextScanned(textScanned).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                textScannedDetailView.onUpdateSuccess()
            }, {
                textScannedDetailView.onError()
            }).let(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}