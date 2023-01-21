package com.ryccoatika.imagetotext.ui.home

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppSearchTextInput
import com.ryccoatika.imagetotext.ui.common.ui.FabImagePicker
import com.ryccoatika.imagetotext.ui.common.ui.ScannedTextCard
import com.ryccoatika.imagetotext.ui.common.utils.rememberStateWithLifecycle

@Composable
fun Home(
    savedStateHandle: SavedStateHandle?,
    openImageResultScreen: (Long) -> Unit,
    openLanguageSelectorScreen: () -> Unit
) {
    Home(
        viewModel = hiltViewModel(),
        savedStateHandle = savedStateHandle,
        openImageResultScreen = openImageResultScreen,
        openLanguageSelectorScreen = openLanguageSelectorScreen
    )
}

@Composable
private fun Home(
    viewModel: HomeViewModel,
    savedStateHandle: SavedStateHandle?,
    openImageResultScreen: (Long) -> Unit,
    openLanguageSelectorScreen: () -> Unit
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    LaunchedEffect(Unit) {
        savedStateHandle?.get<Uri?>("uri")?.let {
            viewModel.setUri(it)
            openLanguageSelectorScreen()
        }
    }

    savedStateHandle?.getStateFlow<String?>("languagemodel", null)
        ?.collectAsState()?.value.let { langModel ->
        savedStateHandle?.set("languagemodel", null)
        if (langModel != null) {
            viewModel.setLanguageModel(langModel)
            viewModel.scanImage()
        }
    }

    viewState.event?.let { event ->
        LaunchedEffect(event) {
            when (event) {
                is HomeViewState.Event.OpenTextScannedDetail -> {
                    openImageResultScreen(event.textScanned.id)
                }
            }
            viewModel.clearEvent()
        }
    }

    Home(
        state = viewState,
        generateImageUri = viewModel::getImageUri,
        onSearchChanged = viewModel::setQuery,
        uriSelected = viewModel::setUri,
        onTextScannedRemove = viewModel::remove,
        openImageResultScreen = openImageResultScreen,
        openLanguageSelectorScreen = openLanguageSelectorScreen
    )
}

@Composable
private fun Home(
    state: HomeViewState,
    generateImageUri: (Context) -> Uri,
    onSearchChanged: (String) -> Unit,
    uriSelected: (Uri) -> Unit,
    onTextScannedRemove: (TextScanned) -> Unit,
    openImageResultScreen: (Long) -> Unit,
    openLanguageSelectorScreen: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FabImagePicker(
                pickedFromGallery = { uri ->
                    uriSelected(uri)
                    openLanguageSelectorScreen()
                },
                pickedFromCamera = { uri ->
                    uriSelected(uri)
                    openLanguageSelectorScreen()
                },
                generateImageUri = generateImageUri
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.large)
        ) {
            Text(
                stringResource(id = R.string.title_your_documents),
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(top = MaterialTheme.spacing.extraLarge)
            )
            AppSearchTextInput(
                value = state.query ?: "",
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = MaterialTheme.spacing.medium)
            ) {
                items(
                    items = state.textScannedCollection,
                    key = {
                        it.id
                    }
                ) { textScanned ->
                    ScannedTextCard(
                        text = textScanned.text,
                        onDismissed = {
                            onTextScannedRemove(textScanned)
                        },
                        modifier = Modifier
                            .padding(bottom = MaterialTheme.spacing.small)
                            .clickable {
                                openImageResultScreen(textScanned.id)
                            }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    AppTheme {
        Home(
            state = HomeViewState.Empty,
            generateImageUri = { Uri.EMPTY },
            onSearchChanged = {},
            uriSelected = {},
            onTextScannedRemove = {},
            openImageResultScreen = {},
            openLanguageSelectorScreen = {}
        )
    }
}