package com.ryccoatika.imagetotext.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.usecase.GetTextFromImage
import com.ryccoatika.imagetotext.domain.usecase.ObserveTextScanned
import com.ryccoatika.imagetotext.domain.usecase.RemoveTextScanned
import com.ryccoatika.imagetotext.domain.usecase.SaveTextScanned
import com.ryccoatika.imagetotext.domain.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeViewModel @Inject constructor(
    observeTextScanned: ObserveTextScanned,
    @ApplicationContext
    private val context: Context,
    private val getTextFromImage: GetTextFromImage,
    private val saveTextScanned: SaveTextScanned,
    private val removeTextScanned: RemoveTextScanned,
    private val composeFileProvider: ComposeFileProvider
) : ViewModel() {

    private val loadingState = ObservableLoadingCounter()
    private val event = MutableStateFlow<HomeViewState.Event?>(null)
    private val recognitionLanguageModel = MutableStateFlow(RecognationLanguageModel.LATIN)
    private val uri = MutableStateFlow(Uri.EMPTY)

    private val query = MutableStateFlow<String?>(null)

    val state: StateFlow<HomeViewState> = combine(
        query,
        observeTextScanned.isProcessing,
        loadingState.observable,
        observeTextScanned.flow,
        event,
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

    fun setLanguageModel(langModel: String) {
        recognitionLanguageModel.value = RecognationLanguageModel.valueOf(langModel)
    }

    fun setUri(uri: Uri) {
        this.uri.value = uri
    }

    fun scanImage() {
        viewModelScope.launch {
            val textRecognized = getTextFromImage.executeSync(
                GetTextFromImage.Params(
                    inputImage = InputImage.fromFilePath(context, uri.value),
                    languageModel = recognitionLanguageModel.value
                )
            )
            val text = textRecognized.textBlocks.joinToString("\n\n") { textBlock ->
                textBlock.lines.joinToString("\n") { line ->
                    line.elements.joinToString(" ") { it.text }
                }
            }
            val textScanned = saveTextScanned.executeSync(
                SaveTextScanned.Params(
                    imageUri = uri.value,
                    textRecognized = textRecognized,
                    text = text
                )
            )
            event.value = HomeViewState.Event.OpenTextScannedDetail(textScanned)
        }
    }

    fun remove(textScanned: TextScanned) {
        viewModelScope.launch {
            removeTextScanned(RemoveTextScanned.Params(textScanned)).collectStatus(
                loadingState
            )
        }
    }

    fun getImageUri(context: Context): Uri = composeFileProvider.getImageUri(context)

    fun clearEvent() {
        event.value = null
    }
}