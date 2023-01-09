package com.ryccoatika.imagetotext.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

private val AppDarkColorScheme = darkColorScheme()

private val AppLightColorScheme = lightColorScheme()

private val AppShape = Shapes()

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
        colorScheme = colors,
        shapes = AppShape,
        typography = AppType,
        content = content
    )
}
