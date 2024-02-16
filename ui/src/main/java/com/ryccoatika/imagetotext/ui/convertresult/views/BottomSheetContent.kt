package com.ryccoatika.imagetotext.ui.convertresult.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ryccoatika.imagetotext.ui.common.theme.spacing
import com.ryccoatika.imagetotext.ui.common.ui.AppTextInput

@Composable
internal fun BottomSheetContent(
    text: String,
    textChanged: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .border(
                1.dp,
                MaterialTheme.colors.secondary,
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                ),
            )
            .sizeIn(
                maxHeight = LocalConfiguration.current.screenHeightDp.dp + WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateBottomPadding(),
            )
            .padding(MaterialTheme.spacing.medium)
            .imePadding()
            .navigationBarsPadding(),
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(7.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.secondary),
        )
        AppTextInput(
            value = text,
            onValueChange = textChanged,
            borderShape = MaterialTheme.shapes.small,
            borderWidth = 2.dp,
            borderColor = MaterialTheme.colors.secondary.copy(ContentAlpha.disabled),
            textColor = Color.Black,
            cursorColor = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.spacing.medium),
        )
    }
}
