package com.ryccoatika.imagetotext.domain.utils

import com.ryccoatika.imagetotext.domain.model.InvokeError
import com.ryccoatika.imagetotext.domain.model.InvokeStarted
import com.ryccoatika.imagetotext.domain.model.InvokeStatus
import com.ryccoatika.imagetotext.domain.model.InvokeSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class ObservableLoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

suspend fun Flow<InvokeStatus>.collectStatus(
    counter: ObservableLoadingCounter,
    uiMessageManager: UiMessageManager? = null
) = collect { status ->
    when (status) {
        is InvokeError -> {
            uiMessageManager?.emitMessage(UiMessage(status.throwable))
            counter.removeLoader()
        }
        InvokeStarted -> counter.addLoader()
        InvokeSuccess -> counter.removeLoader()
    }
}