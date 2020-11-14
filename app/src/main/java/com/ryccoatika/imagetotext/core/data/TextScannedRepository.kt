package com.ryccoatika.imagetotext.core.data

import android.annotation.SuppressLint
import com.ryccoatika.imagetotext.core.data.local.LocalDataSource
import com.ryccoatika.imagetotext.core.domain.model.TextScanned
import com.ryccoatika.imagetotext.core.domain.repository.ITextScannedRepository
import com.ryccoatika.imagetotext.core.utils.toTextScannedDomain
import com.ryccoatika.imagetotext.core.utils.toTextScannedEntity
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class TextScannedRepository(
    private val localDataSource: LocalDataSource
): ITextScannedRepository {

    override fun insertTextScanned(textScanned: TextScanned): Completable =
        localDataSource.insertTextScanned(textScanned.toTextScannedEntity())

    override fun deleteTextScanned(textScanned: TextScanned): Completable =
        localDataSource.deleteTextScanned(textScanned.toTextScannedEntity())

    @SuppressLint("CheckResult")
    override fun getAllTextScanned(): Flowable<Resource<List<TextScanned>>> {
        val resultData = PublishSubject.create<Resource<List<TextScanned>>>()

        val request = localDataSource.getAllTextScanned()

        request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ textScanned ->
                resultData.onNext(
                    if (textScanned.isEmpty())
                        Resource.Empty
                    else
                        Resource.Success(textScanned.map { it.toTextScannedDomain() })
                )
            }, { error ->
                resultData.onNext(Resource.Error(error.message.toString()))
                error.printStackTrace()
            })
        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

    @SuppressLint("CheckResult")
    override fun searchTextScanned(query: String): Flowable<Resource<List<TextScanned>>> {
        val resultData = PublishSubject.create<Resource<List<TextScanned>>>()

        val request = localDataSource.searchTextScanned(query)

        request.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ textScanned ->
                resultData.onNext(
                    if (textScanned.isEmpty())
                        Resource.Empty
                    else
                        Resource.Success(textScanned.map { it.toTextScannedDomain() })
                )
            }, { error ->
                resultData.onNext(Resource.Error(error.message.toString()))
                error.printStackTrace()
            })

        return resultData.toFlowable(BackpressureStrategy.BUFFER)
    }

}