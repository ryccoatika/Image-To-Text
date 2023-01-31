package com.ryccoatika.imagetotext.ui.convertresult

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.domain.usecase.GetTextScanned
import com.ryccoatika.imagetotext.domain.usecase.RemoveTextScanned
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageConvertResultViewModel @Inject constructor(
    getTextScanned: GetTextScanned,
    savedStateHandle: SavedStateHandle,
    private val removeTextScanned: RemoveTextScanned
) : ViewModel() {

    private val id: Long = savedStateHandle["id"]!!
    private val textScanned = MutableStateFlow<TextScanned?>(null)
    private val inputImage: Flow<InputImage?> = textScanned.filterNotNull().map {
        try {
            InputImage.fromBitmap(it.image, 0)
        } catch (e: Exception) {
            null
        }
    }
    private val textElements: Flow<List<TextRecognized.Element>> = textScanned.filterNotNull().map {
        it.textRecognized.textBlocks.fold(
            emptyList()
        ) { acc1, textBlock ->
            acc1 + textBlock.lines.fold(
                emptyList()
            ) { acc2, line ->
                acc2 + line.elements.fold(emptyList()) { acc, element ->
                    acc + element
                }
            }
        }
    }
    private val event = MutableStateFlow<ImageConvertResultViewState.Event?>(null)

    val state: StateFlow<ImageConvertResultViewState> = combine(
        textScanned,
        inputImage,
        textElements,
        event,
        ::ImageConvertResultViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ImageConvertResultViewState.Empty
    )

    init {
        viewModelScope.launch {
            textScanned.value = getTextScanned.executeSync(GetTextScanned.Params(id))
        }
    }

    fun setText(text: String) {
        if (textScanned.value != null) {
            this.textScanned.value = textScanned.value!!.copy(
                textRecognized = textScanned.value!!.textRecognized.copy(
                    text = text
                )
            )
        }
    }

    fun remove() {
        viewModelScope.launch {
            textScanned.value?.let {
                removeTextScanned.executeSync(RemoveTextScanned.Params(it))
                event.value = ImageConvertResultViewState.Event.RemoveSuccess
            }
        }
    }
}