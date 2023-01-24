package com.ryccoatika.imagetotext.ui.imagepreview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppTopBar
import com.ryccoatika.imagetotext.ui.common.utils.rememberStateWithLifecycle

@Composable
fun ImagePreview(
    openImageResultScreen: (Long) -> Unit,
    navigateUp: () -> Unit,
) {

    ImagePreview(
        viewModel = hiltViewModel(),
        openImageResultScreen = openImageResultScreen,
        onNavigateUp = navigateUp
    )
}

@Composable
private fun ImagePreview(
    viewModel: ImagePreviewViewModel,
    openImageResultScreen: (Long) -> Unit,
    onNavigateUp: () -> Unit,
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    viewState.event?.let { event ->
        LaunchedEffect(event) {
            when (event) {
                is ImagePreviewViewState.Event.OpenTextScannedDetail -> {
                    openImageResultScreen(event.textScanned.id)
                }
            }
            viewModel.clearEvent()
        }
    }

    ImagePreview(
        state = viewState,
        onMessageShown = viewModel::clearMessage,
        onLanguageModelChanged = viewModel::setLanguageModel,
        onScanClick = viewModel::scanImage,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ImagePreview(
    state: ImagePreviewViewState,
    onMessageShown: (Long) -> Unit,
    onLanguageModelChanged: (RecognationLanguageModel) -> Unit,
    onScanClick: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val imagePainter = rememberAsyncImagePainter(state.imageUri)
    var expanded by remember { mutableStateOf(false) }

    state.message?.let { message ->
        LaunchedEffect(message) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message.message
            )
            onMessageShown(message.id)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppTopBar(
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = stringResource(R.string.title_preview_image)
            )
        },
        modifier = Modifier.navigationBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
        ) {
            Spacer(Modifier.height(MaterialTheme.spacing.extraLarge))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colors.secondary.copy(0.25f))
            ) {
                Image(
                    imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.large)
                        .border(1.dp, MaterialTheme.colors.secondary, MaterialTheme.shapes.large)

                )
            }
            Spacer(Modifier.height(MaterialTheme.spacing.large))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colors.secondary, MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        state.languageModel?.getText()
                            ?: stringResource(R.string.text_choose_language)
                    )
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    RecognationLanguageModel.values().forEach { recogLangModel ->
                        DropdownMenuItem(
                            onClick = {
                                onLanguageModelChanged(recogLangModel)
                                expanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(recogLangModel.getText())
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onScanClick,
                enabled = state.isValid && !state.processing,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.button_continue))
            }
            if (state.processing) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
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
private fun ImagePreviewPreview() {
    AppTheme {
        ImagePreview(
            state = ImagePreviewViewState.Empty,
            onMessageShown = {},
            onLanguageModelChanged = {},
            onScanClick = {},
            onNavigateUp = {}
        )
    }
}