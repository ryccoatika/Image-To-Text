package com.ryccoatika.imagetotext.ui.common.ui

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScannedTextCard(
    text: String,
    onDismissed: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.onBackground,
                    shape = MaterialTheme.shapes.medium
                )
                .background(MaterialTheme.colors.background)
                .fillMaxWidth()
                .sizeIn(minHeight = 75.dp)
        ) {
            Text(
                text = text,
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
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            onDismissed = {}
        )
    }
}