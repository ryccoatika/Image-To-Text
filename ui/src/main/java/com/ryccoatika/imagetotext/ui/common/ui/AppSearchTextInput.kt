package com.ryccoatika.imagetotext.ui.common.ui

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme

@Composable
fun AppSearchTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppTextInput(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colors.secondary
            )
        },
        placeholder = stringResource(id = R.string.hint_search),
        borderShape = MaterialTheme.shapes.small,
        borderWidth = 1.dp,
        borderColor = MaterialTheme.colors.secondary,
        modifier = modifier
    )
}

@Preview
@Composable
private fun AppSearchTextInputPreview() {
    AppTheme {
        AppSearchTextInput(
            value = "",
            onValueChange = {},
        )
    }
}
