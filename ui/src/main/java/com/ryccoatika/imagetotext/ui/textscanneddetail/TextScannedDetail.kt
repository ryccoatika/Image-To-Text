package com.ryccoatika.imagetotext.ui.textscanneddetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppTextInput

@Composable
fun TextScannedDetail() {
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
                .fillMaxSize()
        ) {
            TextScannedDetailTopBar()
            AppTextInput(
                value = "",
                onValueChange = {},
                backgroundColor = MaterialTheme.colors.primarySurface,
                cursorColor = MaterialTheme.colors.onSurface,
                textColor = MaterialTheme.colors.onSurface,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.small)
                    .fillMaxWidth()
                    .weight(1f)
            )
            TextScannedDetailBottomMenu()
        }
    }
}

@Composable
private fun TextScannedDetailTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { },
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = stringResource(R.string.title_preview),
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.weight(1f),
        )
        IconButton(
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
private fun TextScannedDetailBottomMenu() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = MaterialTheme.spacing.extraLarge)
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colors.primarySurface)
            .padding(
                vertical = MaterialTheme.spacing.large,
                horizontal = MaterialTheme.spacing.extraLarge,
            )
            .fillMaxWidth()
    ) {
        IconButton(
            onClick = { },
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colors.onSurface
            )
        }
        IconButton(
            onClick = { },
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colors.onSurface

            )
        }
        IconButton(
            onClick = { },
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colors.onSurface

            )
        }
    }
}

@Preview
@Composable
private fun TextScannedDetailPreview() {
    AppTheme {
        TextScannedDetail()
    }
}