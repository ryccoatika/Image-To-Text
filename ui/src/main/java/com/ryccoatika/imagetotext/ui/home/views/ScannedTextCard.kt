package com.ryccoatika.imagetotext.ui.home.views

import android.net.Uri
import android.util.Size
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.domain.model.TextScanned
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ScannedTextCard(
    textScanned: TextScanned,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var cardHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    val actionButtonSize = 72.dp
    val actionButtonSizePx = with(density) { actionButtonSize.toPx() }
    val state = remember {
        AnchoredDraggableState(
            initialValue = false,
            anchors = DraggableAnchors {
                false at 0f
                true at actionButtonSizePx * 2
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .fillMaxWidth()
                .height(cardHeight),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Box(
                modifier = Modifier
                    .height(cardHeight)
                    .width(actionButtonSize)
                    .background(Color.Red),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = {
                        onDeleteClick()
                        coroutineScope.launch {
                            state.animateTo(false)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .height(cardHeight)
                    .width(actionButtonSize + 10.dp)
                    .offset {
                        IntOffset(
                            x = -(actionButtonSizePx.toInt() - 10),
                            y = 0,
                        )
                    }
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Blue),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(
                    onClick = {
                        onShareClick()
                        coroutineScope.launch {
                            state.animateTo(false)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 75.dp)
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.secondary,
                    shape = MaterialTheme.shapes.medium,
                )
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.background)
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    reverseDirection = true,
                )
                .onSizeChanged { size ->
                    cardHeight = with(density) { size.height.toDp() }
                },
        ) {
            Image(
                painter = rememberAsyncImagePainter(textScanned.imageUri),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(72.dp)
                    .padding(MaterialTheme.spacing.small)
                    .clip(MaterialTheme.shapes.small),
            )
            Spacer(Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = textScanned.text,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(MaterialTheme.spacing.small),
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
                imageSize = Size(0, 0),
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                textRecognized = TextRecognized(
                    text = "",
                    textBlocks = emptyList(),
                ),
            ),
            onShareClick = {},
            onDeleteClick = {},
        )
    }
}
