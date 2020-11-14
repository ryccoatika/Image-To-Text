package com.ryccoatika.imagetotext.home

import androidx.lifecycle.ViewModel
import com.ryccoatika.imagetotext.core.data.Resource
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import com.ryccoatika.imagetotext.core.domain.usecase.TextScannedUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    private val textScannedInteractor: TextScannedUseCase,
    private val homeView: HomeView
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    fun getAllTextScanned() {
        textScannedInteractor.getAllTextScanned().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { textScanned ->
                when(textScanned) {
                    is Resource.Loading -> homeView.onLoadListTextScannedLoading()
                    is Resource.Error -> homeView.onLoadListTextScannedError(textScanned.message)
                    is Resource.Success -> homeView.onLoadListTextScannedSuccess(textScanned.data)
                    is Resource.Empty -> homeView.onLoadListTextScannedEmpty()
                }
            }.let(compositeDisposable::add)
    }

    fun searchTextScanned(query: String) {
        textScannedInteractor.searchTextScanned(query).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { textScanned ->
                when(textScanned) {
                    is Resource.Loading -> homeView.onLoadListTextScannedLoading()
                    is Resource.Error -> homeView.onLoadListTextScannedError(textScanned.message)
                    is Resource.Success -> homeView.onLoadListTextScannedSuccess(textScanned.data)
                    is Resource.Empty -> homeView.onLoadListTextScannedEmpty()
                }
            }.let(compositeDisposable::add)
    }

    fun insertTextScanned(textScanned: TextScanned) {
        textScannedInteractor.insertTextScanned(textScanned).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                homeView.onInsertSuccess(textScanned)
            }, {
                homeView.onError()
            }).let(compositeDisposable::add)
    }
    fun deleteTextScanned(textScanned: TextScanned) {
        textScannedInteractor.deleteTextScanned(textScanned).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                homeView.onDeleteSuccess()
            }, {
                homeView.onError()
            }).let(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}