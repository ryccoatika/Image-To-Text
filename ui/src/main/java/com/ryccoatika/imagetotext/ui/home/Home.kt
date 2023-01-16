package com.ryccoatika.imagetotext.ui.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppSearchTextInput
import com.ryccoatika.imagetotext.ui.common.ui.FabImagePicker
import com.ryccoatika.imagetotext.ui.common.ui.ScannedTextCard
import com.ryccoatika.imagetotext.ui.common.utils.rememberStateWithLifecycle

@Composable
fun Home() {
    Home(
        viewModel = hiltViewModel()
    )
}

@Composable
private fun Home(
    viewModel: HomeViewModel,
) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    Home(
        state = viewState,
        generateImageUri = viewModel::getImageUri,
        onSearchChanged = viewModel::setQuery
    )
}

@Composable
private fun Home(
    state: HomeViewState,
    generateImageUri: (Context) -> Uri,
    onSearchChanged: (String) -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FabImagePicker(
                pickedFromGallery = { uri ->
                    Log.i("190401", "Home: $uri")
                },
                pickedFromCamera = { uri ->
                    Log.i("190401", "Home: $uri")
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
                        it.dateTime
                    }
                ) { textScanned ->
                    ScannedTextCard(
                        text = textScanned.text,
                        onDismissed = {},
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
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
            onSearchChanged = {}
        )

    }
}