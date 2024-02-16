package com.ryccoatika.imagetotext.ui.convertresult

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
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
import com.ryccoatika.imagetotext.ui.common.ui.AppTopBar
import com.ryccoatika.imagetotext.ui.common.ui.TextHighlightBlock
import com.ryccoatika.imagetotext.ui.common.ui.TextHighlightBlockSelected
import com.ryccoatika.imagetotext.ui.common.utils.rememberStateWithLifecycle
import com.ryccoatika.imagetotext.ui.utils.ReviewHelper
import com.ryccoatika.imagetotext.ui.utils.share
import kotlin.math.roundToInt

@Composable
fun ImageConvertResult(
    navigateBack: () -> Unit,
) {
    ImageConvertResult(
        viewModel = hiltViewModel(),
        navigateBack = navigateBack,
    )
}

@Composable
private fun ImageConvertResult(
    viewModel: ImageConvertResultViewModel,
    navigateBack: () -> Unit,
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    viewState.event?.let { event ->
        LaunchedEffect(event) {
            when (event) {
                ImageConvertResultViewState.Event.RemoveSuccess -> navigateBack()
            }
        }
    }

    ImageConvertResult(
        state = viewState,
        navigateUp = navigateBack,
        textChanged = viewModel::setText,
        onDeleteClick = viewModel::remove,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ImageConvertResult(
    state: ImageConvertResultViewState,
    navigateUp: () -> Unit,
    textChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
) {
    val context = LocalContext.current
    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(Unit) {
        ReviewHelper.launchInAppReview(context as Activity)
    }

    var imageSizeRatio by remember { mutableFloatStateOf(1f) }
    var placeHolderOffset by remember { mutableStateOf(Offset.Zero) }
    val selectedElements = remember { mutableStateListOf<TextRecognized.Element>() }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppTopBar(
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp,
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = stringResource(R.string.title_preview),
                actions = {
                    IconButton(
                        onClick = onDeleteClick,
                    ) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                    }
                    state.textScanned?.text?.let { text ->
                        IconButton(
                            onClick = {
                                context.share(text)
                            },
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                        }
                    }
                },
            )
        },
        sheetBackgroundColor = Color.White,
        sheetShape = RoundedCornerShape(
            topStart = 20.dp,
            topEnd = 20.dp,
        ),
        sheetPeekHeight = 100.dp,
        sheetElevation = 4.dp,
        sheetGesturesEnabled = true,
        sheetContent = {
            ImageConvertResultBottomSheet(
                text = state.textScanned?.text ?: "",
                textChanged = textChanged,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        selectedElements.clear()
                    },
                ),
        ) {
            state.textScanned?.let { textScanned ->
                Image(
                    painter = rememberAsyncImagePainter(textScanned.imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            if (coordinates.size != IntSize.Zero) {
                                // calculate size ratio
                                val widthRatio =
                                    coordinates.size.width / textScanned.imageSize.width.toFloat()
                                val heightRatio =
                                    coordinates.size.height / textScanned.imageSize.height.toFloat()
                                imageSizeRatio = minOf(widthRatio, heightRatio)

                                // calculate offset
                                var yOffset = 0f
                                var xOffset = 0f
                                if ((imageSizeRatio * textScanned.imageSize.width).roundToInt() == coordinates.size.width) {
                                    val scaledImageHeight =
                                        imageSizeRatio * textScanned.imageSize.height
                                    yOffset =
                                        coordinates.size.height / 2 - scaledImageHeight / 2
                                }
                                if ((imageSizeRatio * textScanned.imageSize.height).roundToInt() == coordinates.size.height) {
                                    val scaledImageWidth =
                                        imageSizeRatio * textScanned.imageSize.width
                                    xOffset = coordinates.size.width / 2 - scaledImageWidth / 2
                                }
                                placeHolderOffset = Offset(xOffset, yOffset)
                            }
                        },
                )
            }
            state.elements.forEach { element ->
                TextHighlightBlock(
                    element = element,
                    placeHolderOffset = placeHolderOffset,
                    imageSizeRatio = imageSizeRatio,
                    onLongClick = {
                        selectedElements.clear()
                        selectedElements.add(element)
                    },
                )
            }
            TextHighlightBlockSelected(
                selectedElements = selectedElements,
                elements = state.elements,
                placeHolderOffset = placeHolderOffset,
                imageSizeRatio = imageSizeRatio,
                selectedElementsChanged = { value ->
                    selectedElements.clear()
                    selectedElements.addAll(value)
                },
            )
        }
    }
}

@Composable
private fun ImageConvertResultBottomSheet(
    text: String,
    textChanged: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colors.secondary,
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                ),
            )
            .sizeIn(
                maxHeight = LocalConfiguration.current.screenHeightDp.dp + WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding(),
            )
            .padding(MaterialTheme.spacing.medium)
            .imePadding()
            .navigationBarsPadding(),
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(7.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.secondary),
        )
        AppTextInput(
            value = text,
            onValueChange = textChanged,
            borderShape = MaterialTheme.shapes.small,
            borderWidth = 2.dp,
            borderColor = MaterialTheme.colors.secondary.copy(ContentAlpha.disabled),
            textColor = Color.Black,
            cursorColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium),
        )
    }
}

@Preview
@Composable
private fun ImageConvertResultPreview() {
    AppTheme {
        ImageConvertResult(
            state = ImageConvertResultViewState.Empty,
            navigateUp = {},
            textChanged = {},
            onDeleteClick = {},
        )
    }
}
