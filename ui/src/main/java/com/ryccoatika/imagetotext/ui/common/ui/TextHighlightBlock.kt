package com.ryccoatika.imagetotext.ui.common.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.ryccoatika.imagetotext.domain.model.TextRecognized

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextHighlightBlock(
    element: TextRecognized.Element,
    placeHolderOffset: Offset,
    imageSizeRatio: Float,
    onLongClick: () -> Unit,
) {
    element.boundingBox?.let { rect ->
        LocalDensity.current.run {
            Box(
                modifier = Modifier
                    .offset(
                        x = placeHolderOffset.x.toDp(),
                        y = placeHolderOffset.y.toDp(),
                    )
                    .offset(
                        x = (rect.left * imageSizeRatio).toDp(),
                        y = (rect.top * imageSizeRatio).toDp(),
                    )
                    .size(
                        width = ((rect.right - rect.left) * imageSizeRatio).toDp(),
                        height = ((rect.bottom - rect.top) * imageSizeRatio).toDp(),
                    )
                    .rotate(element.angle)
                    .clip(MaterialTheme.shapes.large)
                    .background(Color.Black.copy(0.2f))
                    .combinedClickable(
                        onClick = {},
                        onLongClick = onLongClick,
                    ),
            )
        }
    }
}
