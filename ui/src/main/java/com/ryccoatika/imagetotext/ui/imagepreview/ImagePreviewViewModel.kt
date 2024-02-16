package com.ryccoatika.imagetotext.ui.imagepreview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Size
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.ryccoatika.imagetotext.domain.exceptions.ImageBroken
import com.ryccoatika.imagetotext.domain.exceptions.TextScanFailure
import com.ryccoatika.imagetotext.domain.exceptions.TextScanNotFound
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.usecase.GetTextFromImage
import com.ryccoatika.imagetotext.domain.usecase.SaveTextScanned
import com.ryccoatika.imagetotext.domain.utils.ObservableLoadingCounter
import com.ryccoatika.imagetotext.domain.utils.UiMessage
import com.ryccoatika.imagetotext.domain.utils.UiMessageManager
import com.ryccoatika.imagetotext.domain.utils.combine
import com.ryccoatika.imagetotext.ui.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class ImagePreviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext
    private val context: Context,
    private val getTextFromImage: GetTextFromImage,
    private val saveTextScanned: SaveTextScanned,
) : ViewModel() {

    private val imageUri: Uri = Uri.parse(savedStateHandle["uri"]!!)
    private val recognitionLanguageModel = MutableStateFlow<RecognationLanguageModel?>(null)
    private val isValid: StateFlow<Boolean> = recognitionLanguageModel.map { it != null }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false,
    )

    private val loadingCounter = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val event = MutableStateFlow<ImagePreviewViewState.Event?>(null)

    val state: StateFlow<ImagePreviewViewState> = combine(
        loadingCounter.observable,
        flowOf(imageUri),
        recognitionLanguageModel,
        isValid,
        uiMessageManager.message,
        event,
        ::ImagePreviewViewState,
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ImagePreviewViewState.Empty,
    )

    fun setLanguageModel(langModel: RecognationLanguageModel) {
        recognitionLanguageModel.value = langModel
    }

    fun scanImage() {
        if (!isValid.value || recognitionLanguageModel.value == null) return
        viewModelScope.launch {
            try {
                loadingCounter.addLoader()
                val inputImage = InputImage.fromFilePath(context, imageUri)
                val textRecognized = getTextFromImage.executeSync(
                    GetTextFromImage.Params(
                        inputImage = inputImage,
                        languageModel = recognitionLanguageModel.value!!,
                    ),
                )
                val text = textRecognized.textBlocks.joinToString("\n\n") { textBlock ->
                    textBlock.lines.joinToString("\n") { line ->
                        line.elements.joinToString(" ") { it.text }
                    }
                }

                val textScanned = saveTextScanned.executeSync(
                    SaveTextScanned.Params(
                        imageUri = imageUri,
                        imageSize = Size(inputImage.width, inputImage.height),
                        textRecognized = textRecognized,
                        text = text,
                    ),
                )
                event.value = ImagePreviewViewState.Event.OpenTextScannedDetail(textScanned)
                loadingCounter.removeLoader()
            } catch (e: TextScanFailure) {
                loadingCounter.removeLoader()
                uiMessageManager.emitMessage(
                    UiMessage(
                        message = context.getString(R.string.error_scan_failure),
                        throwable = e,
                    ),
                )
            } catch (e: TextScanNotFound) {
                loadingCounter.removeLoader()
                uiMessageManager.emitMessage(
                    UiMessage(
                        message = context.getString(R.string.error_scan_not_found),
                        throwable = e,
                    ),
                )
            } catch (e: ImageBroken) {
                loadingCounter.removeLoader()
                uiMessageManager.emitMessage(
                    UiMessage(
                        message = context.getString(R.string.error_image_can_not_be_read),
                        throwable = e,
                    ),
                )
            }
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun clearEvent() {
        event.value = null
    }
}
