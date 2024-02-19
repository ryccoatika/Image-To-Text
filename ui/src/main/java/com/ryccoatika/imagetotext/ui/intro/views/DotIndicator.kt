package com.ryccoatika.imagetotext.ui.intro.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme

@Composable
internal fun DotIndicator(
    count: Int,
    activeIndex: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primaryVariant,
    activeColor: Color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f),
    width: Dp = 10.dp,
    gap: Dp = 5.dp,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .then(if (i != 0) Modifier.padding(start = gap) else Modifier)
                    .width(width)
                    .height(width)
                    .clip(CircleShape)
                    .then(
                        if (i == activeIndex) {
                            Modifier.background(backgroundColor)
                        } else Modifier.background(
                            activeColor,
                        ),
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun DotIndicatorPreview() {
    AppTheme {
        DotIndicator(
            count = 3,
            activeIndex = 0,
        )
    }
}
