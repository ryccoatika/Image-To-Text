package com.ryccoatika.imagetotext.ui.convertresult

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppTextInput
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
        navigateBack = navigateBack,
        textChanged = viewModel::setText
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ImageConvertResult(
    state: ImageConvertResultViewState,
    navigateBack: () -> Unit,
    textChanged: (String) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    var imageSizeRatio by remember { mutableStateOf(1f) }
    var placeHolderOffset by remember { mutableStateOf(Offset.Zero) }
    val selectedElements = remember { mutableStateListOf<TextRecognized.Element>() }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colors.primary,
        sheetBackgroundColor = Color.White,
        sheetShape = MaterialTheme.shapes.small.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        ),
        sheetPeekHeight = 50.dp,
        sheetElevation = 4.dp,
        sheetGesturesEnabled = true,
        sheetContent = {
            ImageConvertResultBottomSheet(
                text = state.textScanned?.text ?: "",
                textChanged =textChanged
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            ImageConvertResultTopBar(
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
                state.textScanned?.let {
                    val imagePainter = rememberAsyncImagePainter(it.imageUri)
                    Image(
                        painter = imagePainter,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .onGloballyPositioned { coordinates ->
                                if (coordinates.size != IntSize.Zero && imagePainter.intrinsicSize != Size.Unspecified && state.inputImage != null) {
                                    // calculate size ratio
                                    val widthRatio =
                                        coordinates.size.width / state.inputImage.width.toFloat()
                                    val heightRatio =
                                        coordinates.size.height / state.inputImage.height.toFloat()
                                    imageSizeRatio = minOf(widthRatio, heightRatio)

                                    // calculate offset
                                    var yOffset = 0f
                                    var xOffset = 0f
                                    if ((imageSizeRatio * state.inputImage.width).roundToInt() == coordinates.size.width) {
                                        val scaledImageHeight =
                                            imageSizeRatio * state.inputImage.height
                                        yOffset = coordinates.size.height / 2 - scaledImageHeight / 2
                                    }
                                    if ((imageSizeRatio * state.inputImage.height).roundToInt() == coordinates.size.height) {
                                        val scaledImageWidth =
                                            imageSizeRatio * state.inputImage.width
                                        xOffset = coordinates.size.width / 2 - scaledImageWidth / 2
                                    }
                                    placeHolderOffset = Offset(xOffset, yOffset)
                                }
                            }
                    )
                }
                state.elements.forEach { element ->
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
                    elements = state.elements,
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
private fun ImageConvertResultBottomSheet(
    text: String,
    textChanged: (String) -> Unit
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(MaterialTheme.spacing.medium)
    ) {
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(5.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        AppTextInput(
            value = text,
            onValueChange = textChanged,
            borderShape = MaterialTheme.shapes.large,
            borderWidth = 2.dp,
            borderColor = MaterialTheme.colors.primary,
            textColor = Color.Black,
            cursorColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium)
        )
        Button(
            onClick = {
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, text)
                    type = "text/plain"
                }.run {
                    context.startActivity(Intent.createChooser(this, null))
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.button_share))
        }
    }
}

@Composable
private fun ImageConvertResultTopBar(
    onBackClick: () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.Start,
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
    }
}

@Preview
@Composable
private fun ImageConvertResultPreview() {
    AppTheme {
        ImageConvertResult(
            state = ImageConvertResultViewState.Empty,
            navigateBack = {},
            textChanged = {}
        )
    }
}