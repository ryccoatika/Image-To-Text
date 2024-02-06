package com.ryccoatika.imagetotext.domain.utils

import com.ryccoatika.imagetotext.domain.model.InvokeError
import com.ryccoatika.imagetotext.domain.model.InvokeStarted
import com.ryccoatika.imagetotext.domain.model.InvokeStatus
import com.ryccoatika.imagetotext.domain.model.InvokeSuccess
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withTimeout

abstract class Interactor<in P> {
    operator fun invoke(
        params: P,
        timeoutMs: Long = defaultTimeoutMs,
    ): Flow<InvokeStatus> = flow {
        try {
            withTimeout(timeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
        } catch (t: TimeoutCancellationException) {
            emit(InvokeError(t))
        }
    }.catch { t -> emit(InvokeError(t)) }

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.SECONDS.toMillis(15)
    }
}

abstract class ResultInteractor<P : Any, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}

abstract class SubjectInteractor<P : Any, T> {
    val isProcessing = MutableStateFlow(false)

    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .onEach { isProcessing.emit(true) }
        .flatMapLatest { createObservable(it) }
        .onEach { isProcessing.emit(false) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}
