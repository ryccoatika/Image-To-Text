package com.ryccoatika.imagetotext.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.usecase.ObserveTextScanned
import com.ryccoatika.imagetotext.domain.usecase.RemoveTextScanned
import com.ryccoatika.imagetotext.domain.usecase.SaveTextScanned
import com.ryccoatika.imagetotext.domain.utils.ObservableLoadingCounter
import com.ryccoatika.imagetotext.domain.utils.UiMessageManager
import com.ryccoatika.imagetotext.domain.utils.collectStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    observeTextScanned: ObserveTextScanned,
    private val saveTextScanned: SaveTextScanned,
    private val removeTextScanned: RemoveTextScanned
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val query = MutableStateFlow<String?>(null)

    val state: StateFlow<HomeViewState> = combine(
        query,
        observeTextScanned.isProcessing,
        loadingState.observable,
        uiMessageManager.message,
        observeTextScanned.flow,
        ::HomeViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeViewState.Empty
    )

    init {
        viewModelScope.launch {
            this@HomeViewModel.query
                .filterNotNull()
                .filterNot { it.isEmpty() }
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    observeTextScanned(ObserveTextScanned.Params(query))
                }
        }

        observeTextScanned(ObserveTextScanned.Params(query.value))
    }

    fun search(query: String) {
        viewModelScope.launch {
            this@HomeViewModel.query.emit(query)
        }
    }

    fun save(textScanned: TextScanned) {
        viewModelScope.launch {
            saveTextScanned(SaveTextScanned.Params(textScanned)).collectStatus(
                loadingState,
                uiMessageManager
            )
        }
    }

    fun remove(textScanned: TextScanned) {
        viewModelScope.launch {
            removeTextScanned(RemoveTextScanned.Params(textScanned)).collectStatus(
                loadingState,
                uiMessageManager
            )
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}