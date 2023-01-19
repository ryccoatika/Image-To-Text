package com.ryccoatika.imagetotext.ui.convertresult

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.domain.utils.combine
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.domain.usecase.GetTextFromImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ImageConvertResultViewModel @Inject constructor(
    @ApplicationContext context: Context,
    savedStateHandle: SavedStateHandle,
    getTextFromImage: GetTextFromImage
) : ViewModel() {

    private val uri = Uri.parse(savedStateHandle["uri"])
    private val inputImage = InputImage.fromFilePath(context, uri)
    private val recognitionLanguageModel = MutableStateFlow(RecognationLanguageModel.LATIN)
    private val textBlocks = MutableStateFlow<List<Text.TextBlock>>(emptyList())
    private val text = MutableStateFlow("")

    val state: StateFlow<ImageConvertResultViewState> = combine(
        flowOf(uri),
        flowOf(inputImage),
        recognitionLanguageModel,
        textBlocks,
        textBlocks.map {
            it.fold(
                emptyList()
            ) { acc1, textBlock ->
                acc1 + textBlock.lines.fold(
                    emptyList()
                ) { acc2, line -> acc2 + line.elements.fold(emptyList()) { acc, element ->
                    acc + element
                } }
            }
        },
        text,
        ::ImageConvertResultViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ImageConvertResultViewState.Empty
    )

    init {
        try {
            viewModelScope.launch {
                textBlocks.value = getTextFromImage.executeSync(
                    GetTextFromImage.Params(
                        inputImage = inputImage,
                        languageModel = recognitionLanguageModel.value
                    )
                )
                text.value = textBlocks.value.joinToString("\n") { textBlock ->
                    textBlock.lines.joinToString("\n") { line ->
                        line.elements.joinToString(" ") { it.text }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        viewModelScope.launch {
            recognitionLanguageModel.collect { langModel ->
                try {
                    textBlocks.value = getTextFromImage.executeSync(
                        GetTextFromImage.Params(
                            inputImage = inputImage,
                            languageModel = langModel
                        )
                    )
                    text.value = textBlocks.value.joinToString("\n\n") { textBlock ->
                        textBlock.lines.joinToString("\n") { line ->
                            line.elements.joinToString(" ") { it.text }
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun setRecognitionLangModel(model: RecognationLanguageModel) {
        viewModelScope.launch {
            recognitionLanguageModel.emit(model)
        }
    }

    fun setText(text: String) {
        this.text.value = text
    }
}