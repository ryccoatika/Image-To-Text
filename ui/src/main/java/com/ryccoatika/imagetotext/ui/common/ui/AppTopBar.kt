package com.ryccoatika.imagetotext.ui.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing

@Composable
fun AppTopBar(
    navigationIcon: (@Composable () -> Unit)? = null,
    title: String? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    TopAppBar(
        contentPadding = WindowInsets.statusBars.only(WindowInsetsSides.Vertical).asPaddingValues(),
        backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colors.onPrimary,
        elevation = 0.dp,
        modifier = Modifier.background(
            Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colors.primary,
                    MaterialTheme.colors.secondary,
                ),
            ),
        ),
    ) {
        navigationIcon?.invoke()
        if (navigationIcon == null) {
            Spacer(Modifier.width(MaterialTheme.spacing.medium))
        }
        if (title != null) {
            Text(
                title,
                style = MaterialTheme.typography.h6,
            )
        }
        Spacer(Modifier.weight(1f))
        actions?.invoke(this)
    }
}

@Preview
@Composable
private fun AppTopBarPreview() {
    AppTheme {
        AppTopBar(
            navigationIcon = {
                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            },
            title = "Title",
            actions = {
                IconButton(
                    onClick = { },
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                }
                IconButton(
                    onClick = {},
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                }
            },
        )
    }
}
