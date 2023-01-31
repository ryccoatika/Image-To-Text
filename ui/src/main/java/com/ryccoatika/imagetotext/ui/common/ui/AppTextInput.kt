package com.ryccoatika.imagetotext.ui.common.ui

import androidx.compose.foundation.border
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme

@Composable
fun AppTextInput(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    backgroundColor: Color = Color.Transparent,
    cursorColor: Color = MaterialTheme.colors.onSurface,
    textColor: Color = MaterialTheme.colors.onSurface,
    leadingIcon: (@Composable () -> Unit)? = null,
    placeholder: String? = null,
    shape: Shape = MaterialTheme.shapes.small,
    borderShape: Shape? = null,
    borderWidth: Dp? = null,
    borderColor: Color? = null
) {
    require(
        (borderShape != null && borderWidth != null && borderColor != null) ||
                (borderShape == null && borderWidth == null && borderColor == null)
    ) { "|borderWidth|, |borderShape|, and |borderColor| must be filled all" }
    TextFieldDefaults.textFieldColors()
    TextField(
        value = value,
        onValueChange = onValueChange,
        shape = MaterialTheme.shapes.large,
        leadingIcon = leadingIcon,
        placeholder = {
            if (placeholder != null) {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
                )
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            backgroundColor = backgroundColor,
            cursorColor = cursorColor,
            textColor = textColor
        ),
        modifier = modifier
            .then(
                if (borderShape != null && borderWidth != null && borderColor != null) Modifier.border(
                    width = borderWidth,
                    color = borderColor,
                    shape = borderShape
                ) else Modifier
            )
            .clip(shape)

    )
}

@Preview
@Composable
private fun AppTextInputPreview() {
    AppTheme {
        AppTextInput(
            value = "",
            onValueChange = {}
        )
    }
}