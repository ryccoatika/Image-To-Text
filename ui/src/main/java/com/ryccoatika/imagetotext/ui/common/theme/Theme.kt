package com.ryccoatika.imagetotext.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val AppDarkColorScheme = darkColors()

private val AppLightColorScheme = darkColors()

private val AppShape = Shapes(
    large = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(15.dp)
)

private val AppType = Typography()

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        AppDarkColorScheme
    } else {
        AppLightColorScheme
    }

    MaterialTheme(
        colors = colors,
        shapes = AppShape,
        typography = AppType,
        content = content
    )
}
