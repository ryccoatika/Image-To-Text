package com.ryccoatika.imagetotext.ui.common.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme

@Composable
fun AppTextInput(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    leadingIcon: (@Composable () -> Unit)? = null,
    placeholder: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = MaterialTheme.shapes.large,
        leadingIcon = leadingIcon,
        placeholder = {
            if (placeholder != null) {
                Text(text = placeholder)
            }
        },
        modifier = modifier
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