package com.ryccoatika.imagetotext.ui.convertresult.views

import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.times
import androidx.core.graphics.toRectF
import com.ryccoatika.imagetotext.domain.model.TextRecognized
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.utils.copyToClipboard

@Composable
internal fun TextHighlightBlockSelected(
    selectedElements: List<TextRecognized.Element>,
    elements: List<TextRecognized.Element>,
    placeHolderOffset: Offset,
    imageSizeRatio: Float,
    selectedElementsChanged: (List<TextRecognized.Element>) -> Unit,
) {
    if (selectedElements.isNotEmpty()) {
        val context = LocalContext.current
        val localTextSelectionColors = LocalTextSelectionColors.current
        var showCopyButton by remember { mutableStateOf(true) }

        val firstElementRect = selectedElements.first().boundingBox!!
        val lastElementRect = selectedElements.last().boundingBox!!

        LocalDensity.current.run {
            AnimatedVisibility(
                visible = showCopyButton,
                enter = scaleIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                exit = scaleOut() + shrinkVertically(shrinkTowards = Alignment.CenterVertically),
            ) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = placeHolderOffset.x.toDp(),
                            y = placeHolderOffset.y.toDp(),
                        )
                        .offset(
                            x = firstElementRect.left
                                .times(imageSizeRatio)
                                .toDp() + 10.dp,
                            y = firstElementRect.top
                                .times(imageSizeRatio)
                                .toDp() - 40.dp,
                        )
                        .clip(MaterialTheme.shapes.large)
                        .background(Color.Black.copy(0.6f)),
                ) {
                    Text(
                        stringResource(R.string.button_copy),
                        color = Color.White,
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.extraSmall)
                            .clickable {
                                selectedElements
                                    .joinToString(
                                        separator = " ",
                                    ) { it.text }
                                    .copyToClipboard(context)
                                showCopyButton = false
                            },
                    )
                }
            }

            SelectionLeftHandle(
                firstElementRect = firstElementRect,
                lastElementRect = lastElementRect,
                imageSizeRatio = imageSizeRatio,
                placeHolderOffset = placeHolderOffset,
                elements = elements,
                selectedElementsChanged = selectedElementsChanged,
            )

            selectedElements.forEach { element ->
                Box(
                    modifier = Modifier
                        .offset(
                            x = placeHolderOffset.x.toDp(),
                            y = placeHolderOffset.y.toDp(),
                        )
                        .offset(
                            x = element.boundingBox!!.left
                                .times(imageSizeRatio)
                                .toDp(),
                            y = element.boundingBox!!.top
                                .times(imageSizeRatio)
                                .toDp(),
                        )
                        .size(
                            width = (
                                (element.boundingBox!!.right - element.boundingBox!!.left)
                                    .times(imageSizeRatio)
                                ).toDp(),
                            height = (
                                (element.boundingBox!!.bottom - element.boundingBox!!.top)
                                    .times(imageSizeRatio)
                                ).toDp(),
                        )
                        .rotate(element.angle)
                        .background(localTextSelectionColors.backgroundColor)
                        .clickable { showCopyButton = !showCopyButton },
                )
            }

            SelectionRightHandle(
                firstElementRect = firstElementRect,
                lastElementRect = lastElementRect,
                imageSizeRatio = imageSizeRatio,
                placeHolderOffset = placeHolderOffset,
                elements = elements,
                selectedElementsChanged = selectedElementsChanged,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SelectionLeftHandle(
    firstElementRect: Rect,
    lastElementRect: Rect,
    imageSizeRatio: Float,
    placeHolderOffset: Offset,
    elements: List<TextRecognized.Element>,
    selectedElementsChanged: (List<TextRecognized.Element>) -> Unit,
    handleSize: Dp = 20.dp,
) {
    val configuration = LocalConfiguration.current
    val localTextSelectionColors = LocalTextSelectionColors.current

    LocalDensity.current.run {
        val handleOffsetX = remember(placeHolderOffset, firstElementRect, imageSizeRatio) {
            placeHolderOffset.x +
                firstElementRect.left.times(imageSizeRatio) -
                handleSize.toPx()
        }
        val handleOffsetY = remember(placeHolderOffset, firstElementRect, imageSizeRatio) {
            placeHolderOffset.y +
                firstElementRect.top.times(imageSizeRatio) -
                handleSize.toPx()
        }

        val rotationYForExceedingScreenWidth = remember(
            configuration.screenWidthDp,
            imageSizeRatio,
            placeHolderOffset.x,
            lastElementRect.right,
        ) {
            val minOffsetX = handleSize.toPx()

            if (handleOffsetX < minOffsetX) {
                180f
            } else {
                0f
            }
        }
        Box(
            modifier = Modifier
                .offset(
                    x = handleOffsetX
                        .let { offset ->
                            if (rotationYForExceedingScreenWidth != 0f) {
                                offset.plus(handleSize.toPx())
                            } else {
                                offset
                            }
                        }
                        .toDp(),
                    y = handleOffsetY.toDp(),
                )
                .graphicsLayer {
                    rotationY = rotationYForExceedingScreenWidth
                }
                .size(handleSize)
                .clip(CircleShape.copy(bottomEnd = ZeroCornerSize))
                .background(localTextSelectionColors.handleColor)
                .pointerInteropFilter { motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {}
                        MotionEvent.ACTION_UP -> {}
                        MotionEvent.ACTION_MOVE -> {
                            if (elements.isNotEmpty()) {
                                selectedElementsChanged(emptyList())

                                var dragX =
                                    firstElementRect.left.times(imageSizeRatio) + motionEvent.x - 20.dp.toPx()
                                var dragY =
                                    firstElementRect.top.times(imageSizeRatio) + motionEvent.y - 20.dp.toPx()

                                if (dragY > lastElementRect.bottom.times(imageSizeRatio)) {
                                    dragY = lastElementRect.top.times(imageSizeRatio)
                                }
                                if (dragX > lastElementRect.right.times(imageSizeRatio)) {
                                    dragX = lastElementRect.left.times(imageSizeRatio)
                                }

                                val firstElementIndex =
                                    elements
                                        .indexOfFirst { element ->
                                            element.boundingBox
                                                ?.toRectF()
                                                ?.times(imageSizeRatio)
                                                ?.intersect(
                                                    RectF(
                                                        dragX,
                                                        dragY,
                                                        dragX,
                                                        dragY,
                                                    ),
                                                ) == true
                                        }
                                        .run {
                                            if (this == -1) {
                                                elements.indexOfFirst { element ->
                                                    val elementX =
                                                        element.boundingBox!!.right.times(
                                                            imageSizeRatio,
                                                        )
                                                    val elementY =
                                                        element.boundingBox!!.bottom.times(
                                                            imageSizeRatio,
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
                                            lastElementIndex + 1,
                                        ),
                                    )
                                }
                            }
                        }

                        else -> return@pointerInteropFilter false
                    }
                    true
                },
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SelectionRightHandle(
    firstElementRect: Rect,
    lastElementRect: Rect,
    imageSizeRatio: Float,
    placeHolderOffset: Offset,
    elements: List<TextRecognized.Element>,
    selectedElementsChanged: (List<TextRecognized.Element>) -> Unit,
    handleSize: Dp = 20.dp,
) {
    val configuration = LocalConfiguration.current
    val localTextSelectionColors = LocalTextSelectionColors.current

    LocalDensity.current.run {
        val handleOffsetX = remember(placeHolderOffset.x, lastElementRect.right, imageSizeRatio) {
            placeHolderOffset.x + lastElementRect.right.times(imageSizeRatio)
        }
        val handleOffsetY = remember(placeHolderOffset.y, lastElementRect.bottom, imageSizeRatio) {
            placeHolderOffset.y + lastElementRect.bottom.times(imageSizeRatio)
        }

        val rotationYForExceedingScreenWidth = remember(
            configuration.screenWidthDp,
            imageSizeRatio,
            handleOffsetX,
            handleSize,
        ) {
            val maxOffsetX = configuration.screenWidthDp.dp.toPx() - handleSize.toPx()

            if (handleOffsetX > maxOffsetX) {
                180f
            } else {
                0f
            }
        }

        Box(
            modifier = Modifier
                .offset(
                    x = handleOffsetX
                        .let { offset ->
                            // if offsetX exceeding screenWidth
                            if (rotationYForExceedingScreenWidth != 0f) {
                                // offsetX minus width of selection handle
                                offset.minus(handleSize.toPx())
                            } else {
                                offset
                            }
                        }
                        .toDp(),
                    y = handleOffsetY.toDp(),
                )
                .graphicsLayer {
                    rotationY = rotationYForExceedingScreenWidth
                }
                .size(handleSize)
                .clip(CircleShape.copy(topStart = ZeroCornerSize))
                .background(localTextSelectionColors.handleColor)
                .pointerInteropFilter { motionEvent ->
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {}
                        MotionEvent.ACTION_UP -> {}
                        MotionEvent.ACTION_MOVE -> {
                            var dragX =
                                lastElementRect.right.times(imageSizeRatio) + motionEvent.x
                            var dragY =
                                lastElementRect.bottom.times(imageSizeRatio) + motionEvent.y

                            if (elements.isNotEmpty()) {
                                selectedElementsChanged(emptyList())

                                if (dragY < firstElementRect.top.times(imageSizeRatio)) {
                                    dragY = firstElementRect.bottom.times(imageSizeRatio)
                                }
                                if (dragX < firstElementRect.left.times(imageSizeRatio)) {
                                    dragX = firstElementRect.right.times(imageSizeRatio)
                                }

                                val firstElementIndex =
                                    elements.indexOfFirst { element -> element.boundingBox == firstElementRect }

                                val lastElementIndex = elements
                                    .indexOfLast { element ->
                                        element.boundingBox?.intersect(
                                            Rect(
                                                dragX.toInt(),
                                                dragY.toInt(),
                                                dragX.toInt(),
                                                dragY.toInt(),
                                            ),
                                        ) == true
                                    }
                                    .run {
                                        if (this == -1) {
                                            elements.indexOfLast { element ->
                                                val elementX =
                                                    element.boundingBox!!.left.times(
                                                        imageSizeRatio,
                                                    )
                                                val elementY =
                                                    element.boundingBox!!.top.times(
                                                        imageSizeRatio,
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
                                            lastElementIndex + 1,
                                        ),
                                    )
                                }
                            }
                        }

                        else -> return@pointerInteropFilter false
                    }
                    true
                },
        )
    }
}
