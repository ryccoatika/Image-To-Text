package com.ryccoatika.imagetotext.ui.textscanneddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.usecase.RemoveTextScanned
import com.ryccoatika.imagetotext.domain.usecase.SaveTextScanned
import com.ryccoatika.imagetotext.domain.utils.ObservableLoadingCounter
import com.ryccoatika.imagetotext.domain.utils.UiMessageManager
import com.ryccoatika.imagetotext.domain.utils.collectStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TextScannedDetailViewModel @Inject constructor(
    private val removeTextScanned: RemoveTextScanned,
    private val saveTextScanned: SaveTextScanned,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: Long = savedStateHandle[TextScannedDetailArgs.ID]!!

    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    private val mode = MutableStateFlow(TextScannedDetailViewState.Mode.VIEW)

    val state: StateFlow<TextScannedDetailViewState> = combine(
        mode,
        flowOf(),
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

    fun toggleMode() {
        viewModelScope.launch {
            if (mode.value == TextScannedDetailViewState.Mode.VIEW) {
                mode.emit(TextScannedDetailViewState.Mode.EDIT)
            } else {
                mode.emit(TextScannedDetailViewState.Mode.VIEW)
            }
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}