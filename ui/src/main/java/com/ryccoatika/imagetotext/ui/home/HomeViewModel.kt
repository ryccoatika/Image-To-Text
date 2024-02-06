package com.ryccoatika.imagetotext.ui.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.usecase.ObserveTextScanned
import com.ryccoatika.imagetotext.domain.usecase.RemoveTextScanned
import com.ryccoatika.imagetotext.domain.utils.ComposeFileProvider
import com.ryccoatika.imagetotext.domain.utils.ObservableLoadingCounter
import com.ryccoatika.imagetotext.domain.utils.collectStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    observeTextScanned: ObserveTextScanned,
    private val removeTextScanned: RemoveTextScanned,
    private val composeFileProvider: ComposeFileProvider,
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val query = MutableStateFlow<String?>(null)

    val state: StateFlow<HomeViewState> = combine(
        query,
        observeTextScanned.isProcessing,
        loadingState.observable,
        observeTextScanned.flow,
        ::HomeViewState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeViewState.Empty,
    )

    init {
        viewModelScope.launch {
            this@HomeViewModel.query
                .filterNotNull()
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    observeTextScanned(ObserveTextScanned.Params(query))
                }
        }

        observeTextScanned(ObserveTextScanned.Params(query.value))
    }

    fun setQuery(query: String) {
        this.query.value = query
    }

    fun remove(textScanned: TextScanned) {
        viewModelScope.launch {
            removeTextScanned(RemoveTextScanned.Params(textScanned)).collectStatus(
                loadingState,
            )
        }
    }

    fun getImageUri(context: Context): Uri = composeFileProvider.getImageUri(context)
}
