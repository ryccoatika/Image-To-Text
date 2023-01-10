package com.ryccoatika.imagetotext.ui.textscanneddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.usecase.SaveTextScanned
import com.ryccoatika.imagetotext.domain.usecase.RemoveTextScanned
import com.ryccoatika.imagetotext.domain.utils.ObservableLoadingCounter
import com.ryccoatika.imagetotext.domain.utils.UiMessageManager
import com.ryccoatika.imagetotext.domain.utils.collectStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TextScannedDetailViewModel @Inject constructor(
    private val removeTextScanned: RemoveTextScanned,
    private val saveTextScanned: SaveTextScanned
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    val state: StateFlow<TextScannedDetailViewState> = combine(
        loadingState.observable,
        uiMessageManager.message,
        ::TextScannedDetailViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TextScannedDetailViewState.Empty
    )

    fun remove(textScanned: TextScanned) {
        viewModelScope.launch {
            removeTextScanned(RemoveTextScanned.Params(textScanned)).collectStatus(
                loadingState,
                uiMessageManager
            )
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
}