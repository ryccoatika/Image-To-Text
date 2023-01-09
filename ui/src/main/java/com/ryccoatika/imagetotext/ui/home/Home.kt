package com.ryccoatika.imagetotext.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppSearchTextInput
import com.ryccoatika.imagetotext.ui.common.ui.ScannedTextCard

@Composable
fun Home() {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
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
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = MaterialTheme.spacing.medium)
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = MaterialTheme.spacing.medium)
            ) {
                items(count = 10) {
                    ScannedTextCard(
                        text = "",
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
        Home()
    }
}