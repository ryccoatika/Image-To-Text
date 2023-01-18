package com.ryccoatika.imagetotext.ui.common.ui

import android.graphics.Rect
import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.text.Text
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.spacing


@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun TextHighlightBlockSelected(
    selectedElements: List<Text.Element>,
    elements: List<Text.Element>,
    placeHolderOffset: Offset,
    imageSizeRatio: Float,
    selectedElementsChanged: (List<Text.Element>) -> Unit
) {
    if (selectedElements.isNotEmpty()) {
        val clipboardManager = LocalClipboardManager.current
        val localTextSelectionColors = LocalTextSelectionColors.current
        var showCopyButton by remember { mutableStateOf(true) }

        val firstElementRect = selectedElements.first().boundingBox!!
        val lastElementRect = selectedElements.last().boundingBox!!

        LocalDensity.current.run {
            AnimatedVisibility(
                visible = showCopyButton,
                enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically)
            ) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = placeHolderOffset.x.toDp(),
                            y = placeHolderOffset.y.toDp()
                        )
                        .offset(
                            x = firstElementRect.left
                                .times(imageSizeRatio)
                                .toDp() + 10.dp,
                            y = firstElementRect.top
                                .times(imageSizeRatio)
                                .toDp() - 40.dp
                        )
                        .clip(MaterialTheme.shapes.large)
                        .background(Color.Black.copy(0.6f))
                ) {
                    Text(
                        stringResource(R.string.button_copy),
                        color = Color.White,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.extraSmall)
                            .clickable {
                                clipboardManager.setText(
                                    AnnotatedString(
                                        selectedElements.joinToString(
                                            separator = " "
                                        ) { it.text })
                                )
                                showCopyButton = false
                            }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .offset(
                        x = placeHolderOffset.x.toDp(),
                        y = placeHolderOffset.y.toDp()
                    )
                    .offset(
                        x = firstElementRect.left
                            .times(imageSizeRatio)
                            .toDp() - 20.dp,
                        y = firstElementRect.top
                            .times(imageSizeRatio)
                            .toDp() - 20.dp
                    )
                    .size(20.dp)
                    .clip(CircleShape.copy(bottomEnd = ZeroCornerSize))
                    .background(localTextSelectionColors.handleColor)
                    .pointerInteropFilter { motionEvent ->
                        when (motionEvent.action) {
                            MotionEvent.ACTION_DOWN -> {}
                            MotionEvent.ACTION_UP -> {}
                            MotionEvent.ACTION_MOVE -> {
                                if (elements.isNotEmpty()) {
                                    selectedElementsChanged(emptyList())

                                    val dragX =
                                        firstElementRect.left.times(imageSizeRatio) + motionEvent.x - 20.dp.toPx()
                                    val dragY =
                                        firstElementRect.bottom.times(imageSizeRatio) + motionEvent.y - 20.dp.toPx()

                                    val firstElementIndex =
                                        elements
                                            .indexOfFirst { element ->
                                                element.boundingBox?.intersect(
                                                    Rect(
                                                        dragX.toInt(),
                                                        dragY.toInt(),
                                                        dragX.toInt(),
                                                        dragY.toInt()
                                                    )
                                                ) == true
                                            }
                                            .run {
                                                if (this == -1) {
                                                    elements.indexOfFirst { element ->
                                                        val elementX =
                                                            element.boundingBox!!.right.times(
                                                                imageSizeRatio
                                                            )
                                                        val elementY =
                                                            element.boundingBox!!.bottom.times(
                                                                imageSizeRatio
                                                            )

                                                        dragX < elementX && dragY < elementY
                                                    }

                                                } else {
                                                    this
                                                }
                                            }

                                    val lastElementIndex =
                                        elements.indexOfLast { element -> element.boundingBox == lastElementRect }
                                    if (firstElementIndex != -1 && lastElementIndex != -1 && firstElementIndex <= lastElementIndex) {
                                        selectedElementsChanged(
                                            elements.subList(
                                                firstElementIndex,
                                                lastElementIndex + 1
                                            )
                                        )
                                    }
                                }

                            }
                            else -> return@pointerInteropFilter false
                        }
                        true
                    }
            )
            selectedElements.forEach { element ->
                Box(
                    modifier = Modifier
                        .offset(
                            x = placeHolderOffset.x.toDp(),
                            y = placeHolderOffset.y.toDp()
                        )
                        .offset(
                            x = element.boundingBox!!.left
                                .times(imageSizeRatio)
                                .toDp(),
                            y = element.boundingBox!!.top
                                .times(imageSizeRatio)
                                .toDp()
                        )
                        .size(
                            width = ((element.boundingBox!!.right - element.boundingBox!!.left).times(
                                imageSizeRatio
                            )).toDp(),
                            height = ((element.boundingBox!!.bottom - element.boundingBox!!.top).times(
                                imageSizeRatio
                            )).toDp()
                        )
                        .rotate(element.angle)
                        .background(localTextSelectionColors.backgroundColor)
                        .clickable { showCopyButton = !showCopyButton }
                )
            }
            Box(
                modifier = Modifier
                    .offset(
                        x = placeHolderOffset.x.toDp(),
                        y = placeHolderOffset.y.toDp()
                    )
                    .offset(
                        x = lastElementRect.right
                            .times(imageSizeRatio)
                            .toDp(),
                        y = lastElementRect.bottom
                            .times(imageSizeRatio)
                            .toDp()
                    )
                    .size(20.dp)
                    .clip(CircleShape.copy(topStart = ZeroCornerSize))
                    .background(localTextSelectionColors.handleColor)
                    .pointerInteropFilter { motionEvent ->
                        when (motionEvent.action) {
                            MotionEvent.ACTION_DOWN -> {}
                            MotionEvent.ACTION_UP -> {}
                            MotionEvent.ACTION_MOVE -> {
                                val dragX =
                                    lastElementRect.right.times(imageSizeRatio) + motionEvent.x
                                val dragY =
                                    lastElementRect.bottom.times(imageSizeRatio) + motionEvent.y

                                if (elements.isNotEmpty()) {
                                    selectedElementsChanged(emptyList())

                                    val firstElementIndex =
                                        elements.indexOfFirst { element -> element.boundingBox == firstElementRect }
                                    val lastElementIndex = elements
                                        .indexOfLast { element ->
                                            element.boundingBox?.intersect(
                                                Rect(
                                                    dragX.toInt(),
                                                    dragY.toInt(),
                                                    dragX.toInt(),
                                                    dragY.toInt()
                                                )
                                            ) == true
                                        }
                                        .run {
                                            if (this == -1) {
                                                elements.indexOfLast { element ->
                                                    val elementX =
                                                        element.boundingBox!!.left.times(
                                                            imageSizeRatio
                                                        )
                                                    val elementY =
                                                        element.boundingBox!!.top.times(
                                                            imageSizeRatio
                                                        )

                                                    elementX < dragX && elementY < dragY
                                                }
                                            } else {
                                                this
                                            }
                                        }

                                    if (firstElementIndex != -1 && lastElementIndex != -1 && firstElementIndex <= lastElementIndex) {
                                        selectedElementsChanged(
                                            elements.subList(
                                                firstElementIndex,
                                                lastElementIndex + 1
                                            )
                                        )
                                    }
                                }

                            }
                            else -> return@pointerInteropFilter false
                        }
                        true
                    }

            )
        }

    }
}