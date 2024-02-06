package com.ryccoatika.imagetotext.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val AppDarkColorScheme = darkColors()

private val AppLightColorScheme = lightColors(
    background = Color.White,
    onBackground = Color(0xFF475569),
    primary = Color(0xFF465BD8),
    onPrimary = Color.White,
    secondary = Color(0xFF73A5F7),
    onSecondary = Color.White,
)

private val AppShape = Shapes(
    large = RoundedCornerShape(5.dp),
    medium = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
)

private val AppType = Typography()

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
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
        content = content,
    )
}
