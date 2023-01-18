package com.ryccoatika.imagetotext.ui.convertresult

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.TextHighlightBlock
import com.ryccoatika.imagetotext.ui.common.ui.TextHighlightBlockSelected
import com.ryccoatika.imagetotext.ui.common.utils.rememberStateWithLifecycle
import kotlin.math.roundToInt

@Composable
fun ImageConvertResult(
    navigateBack: () -> Unit
) {
    ImageConvertResult(
        viewModel = hiltViewModel(),
        navigateBack = navigateBack
    )
}


@Composable
private fun ImageConvertResult(
    viewModel: ImageConvertResultViewModel,
    navigateBack: () -> Unit
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    ImageConvertResult(
        state = viewState,
        languageModelChanged = viewModel::setRecognitionLangModel,
        navigateBack = navigateBack
    )
}

@Composable
private fun ImageConvertResult(
    state: ImageConvertResultViewState,
    languageModelChanged: (RecognationLanguageModel) -> Unit,
    navigateBack: () -> Unit
) {
    var imageSizeRatio by remember { mutableStateOf(1f) }
    var placeHolderOffset by remember { mutableStateOf(Offset.Zero) }
    val selectedElements = remember { mutableStateListOf<Text.Element>() }

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ImageConvertResultTopBar(
                state = state,
                languageModelChanged = languageModelChanged,
                onBackClick = navigateBack
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            selectedElements.clear()
                        }
                    )
            ) {
                val imagePainter = rememberAsyncImagePainter(state.imageUri)
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            if (coordinates.size != IntSize.Zero && imagePainter.intrinsicSize != Size.Unspecified) {
                                // calculate size ratio
                                val widthRatio =
                                    coordinates.size.width / imagePainter.intrinsicSize.width
                                val heightRatio =
                                    coordinates.size.height / imagePainter.intrinsicSize.height
                                imageSizeRatio = minOf(widthRatio, heightRatio)

                                // calculate offset
                                var yOffset = 0f
                                var xOffset = 0f
                                if ((imageSizeRatio * imagePainter.intrinsicSize.width).roundToInt() == coordinates.size.width) {
                                    val scaledImageHeight =
                                        imageSizeRatio * imagePainter.intrinsicSize.height
                                    yOffset = coordinates.size.height / 2 - scaledImageHeight / 2
                                }
                                if ((imageSizeRatio * imagePainter.intrinsicSize.height).roundToInt() == coordinates.size.height) {
                                    val scaledImageWidth =
                                        imageSizeRatio * imagePainter.intrinsicSize.width
                                    xOffset = coordinates.size.width / 2 - scaledImageWidth / 2
                                }
                                placeHolderOffset = Offset(xOffset, yOffset)
                            }
                        }
                )
                state.texts.forEach { element ->
                    TextHighlightBlock(
                        element = element,
                        placeHolderOffset = placeHolderOffset,
                        imageSizeRatio = imageSizeRatio
                    ) {
                        selectedElements.clear()
                        selectedElements.add(element)
                    }
                }
                TextHighlightBlockSelected(
                    selectedElements = selectedElements,
                    elements = state.texts,
                    placeHolderOffset = placeHolderOffset,
                    imageSizeRatio = imageSizeRatio,
                    selectedElementsChanged = { value ->
                        selectedElements.clear()
                        selectedElements.addAll(value)
                    }
                )
            }
        }
    }
}

@Composable
private fun ImageConvertResultTopBar(
    state: ImageConvertResultViewState,
    languageModelChanged: (RecognationLanguageModel) -> Unit,
    onBackClick: () -> Unit
) {
    var showDropdown by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(MaterialTheme.spacing.medium)
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClick,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(40.dp)
            )
        }

        Box {
            Row(modifier = Modifier.clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                showDropdown = true
            }) {
                Text(
                    state.language.getText()
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(expanded = showDropdown,
                onDismissRequest = { showDropdown = false }) {
                RecognationLanguageModel.values().forEach { recogLangModel ->
                    DropdownMenuItem(onClick = {
                        languageModelChanged(recogLangModel)
                    }) {
                        Text(recogLangModel.getText())
                    }
                }
            }
        }
    }
}

@Composable
private fun RecognationLanguageModel.getText(): String {
    return stringResource(
        when (this) {
            RecognationLanguageModel.LATIN -> R.string.lang_latin
            RecognationLanguageModel.CHINESE -> R.string.lang_chinese
            RecognationLanguageModel.JAPANESE -> R.string.lang_japanese
            RecognationLanguageModel.KOREAN -> R.string.lang_korean
            RecognationLanguageModel.DEVANAGARI -> R.string.lang_devanagari
        }
    )
}

@Preview
@Composable
private fun ImageConvertResultPreview() {
    AppTheme {
        ImageConvertResult(
            state = ImageConvertResultViewState.Empty,
            languageModelChanged = {},
            navigateBack = {}
        )
    }
}