package com.ryccoatika.imagetotext.ui.common.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScannedTextCard(
    textScanned: TextScanned,
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imagePainter = rememberAsyncImagePainter(textScanned.imageUri)

    val dismissState = rememberDismissState(confirmStateChange = {
        onDismissed()
        true
    })

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = {
            FractionalThreshold(0.5f)
        },
        modifier = modifier,
        background = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Red)
                    .fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(end = MaterialTheme.spacing.medium)
                        .size(40.dp)
                )
            }
        },
    ) {
        Row(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.secondary,
                    shape = MaterialTheme.shapes.medium
                )
                .background(MaterialTheme.colors.background)
                .fillMaxWidth()
                .sizeIn(minHeight = 75.dp)
        ) {
            Image(
                imagePainter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(72.dp)
                    .padding(MaterialTheme.spacing.small)
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = textScanned.text,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.small)
            )
        }
    }
}

@Preview
@Composable
private fun ScannedTextCardPreview() {
    AppTheme {
        ScannedTextCard(
            textScanned = TextScanned(
                id = 0,
                imageUri = Uri.EMPTY,
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                textRecognized = TextRecognized(
                    text = "",
                    textBlocks = emptyList()
                )
            ),
            onDismissed = {}
        )
    }
}