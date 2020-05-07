package com.ryccoatika.imagetotext.home

import android.content.Context
import com.ryccoatika.imagetotext.db.AppDatabase
import com.ryccoatika.imagetotext.db.TextScannedEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(context: Context, private val view: HomeView) {

    private val disposable = CompositeDisposable()
    private var database: AppDatabase? = null

    init {
        database = AppDatabase.getInstance(context)
    }

    fun insertTextScanned(text: String) {
        val textScanned = TextScannedEntity(text = text)
        database?.let { appDatabase ->
            appDatabase.textScannedDao().insert(textScanned).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    textScanned.id = it.toInt()
                    view.onHideLoading()
                    view.onInsertCompleted(textScanned)
                }, { error ->
                    view.onHideLoading()
                    view.onInsertFailure(error)
                }).also {
                    disposable.add(it)
                }
        }
    }

    fun deleteTextScanned(textScanned: TextScannedEntity) {
        database?.let { appDatabase ->
            appDatabase.textScannedDao().delete(textScanned).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view.onHideLoading()
                    view.onDeleteCompleted()
                }, { error ->
                    view.onHideLoading()
                    view.onDeleteFailure(error)
                }).also {
                    disposable.add(it)
                }
        }
    }

    fun loadTextScanned() {
        view.onShowLoading()
        database?.let { appDatabase ->
            appDatabase.textScannedDao().getTexts().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    view.onHideLoading()
                    view.onGetTextScannedResponse(response)
                }, { error ->
                    view.onHideLoading()
                    view.onGetTextScannedFailure(error)
                }).also {
                    disposable.add(it)
                }
        }

    }

    fun searchTextScanned(search: String) {
        val s = "%$search%"
        database?.let { appDatabase ->
            appDatabase.textScannedDao().searchTexts(s).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ results ->
                    view.onSearchTextScannedResponse(results)
                }, { error ->
                    view.onSearchTextScannedFailure(error)
                }).also {
                    disposable.add(it)
                }
        }
    }

    fun destroy() {
        AppDatabase.destroyDatabase()
        disposable.dispose()
        disposable.clear()
    }
}