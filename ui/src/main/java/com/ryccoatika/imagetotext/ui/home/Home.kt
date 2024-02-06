package com.ryccoatika.imagetotext.ui.home

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppSearchTextInput
import com.ryccoatika.imagetotext.ui.common.ui.AppTopBar
import com.ryccoatika.imagetotext.ui.common.ui.FabImagePicker
import com.ryccoatika.imagetotext.ui.common.ui.ScannedTextCard
import com.ryccoatika.imagetotext.ui.common.utils.rememberStateWithLifecycle

@Composable
fun Home(
    openImageResultScreen: (Long) -> Unit,
    openImagePreviewScreen: (Uri) -> Unit,
) {
    Home(
        viewModel = hiltViewModel(),
        openImageResultScreen = openImageResultScreen,
        openImagePreviewScreen = openImagePreviewScreen,
    )
}

@Composable
private fun Home(
    viewModel: HomeViewModel,
    openImageResultScreen: (Long) -> Unit,
    openImagePreviewScreen: (Uri) -> Unit,
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    Home(
        state = viewState,
        generateImageUri = viewModel::getImageUri,
        onSearchChanged = viewModel::setQuery,
        onTextScannedRemove = viewModel::remove,
        openImageResultScreen = openImageResultScreen,
        openLanguageSelectorScreen = openImagePreviewScreen,
    )
}

@Composable
private fun Home(
    state: HomeViewState,
    generateImageUri: (Context) -> Uri,
    onSearchChanged: (String) -> Unit,
    onTextScannedRemove: (TextScanned) -> Unit,
    openImageResultScreen: (Long) -> Unit,
    openLanguageSelectorScreen: (Uri) -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.app_name),
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FabImagePicker(
                pickedFromGallery = { uri ->
                    openLanguageSelectorScreen(uri)
                },
                pickedFromCamera = { uri ->
                    openLanguageSelectorScreen(uri)
                },
                generateImageUri = generateImageUri,
            )
        },
        modifier = Modifier
            .navigationBarsPadding(),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.medium),
        ) {
            Spacer(Modifier.height(MaterialTheme.spacing.medium))
            Text(
                stringResource(id = R.string.title_your_documents),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
            )
            Spacer(Modifier.height(MaterialTheme.spacing.small))
            AppSearchTextInput(
                value = state.query ?: "",
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .fillMaxWidth(),
            )
            if (state.textScannedCollection.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = MaterialTheme.spacing.medium),
                ) {
                    items(
                        items = state.textScannedCollection,
                        key = {
                            it.id
                        },
                    ) { textScanned ->
                        ScannedTextCard(
                            textScanned = textScanned,
                            onDismissed = {
                                onTextScannedRemove(textScanned)
                            },
                            modifier = Modifier
                                .padding(bottom = MaterialTheme.spacing.small)
                                .clickable {
                                    openImageResultScreen(textScanned.id)
                                },
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        painterResource(R.drawable.decoration_empty),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium),
                    )
                    Spacer(Modifier.height(32.dp))
                    Text(
                        stringResource(R.string.text_lets_scan),
                        color = MaterialTheme.colors.secondary,
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
            onTextScannedRemove = {},
            openImageResultScreen = {},
            openLanguageSelectorScreen = {},
        )
    }
}
