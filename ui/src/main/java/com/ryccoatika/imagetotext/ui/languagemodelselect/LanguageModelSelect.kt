package com.ryccoatika.imagetotext.ui.languagemodelselect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ryccoatika.imagetotext.domain.model.RecognationLanguageModel
import com.ryccoatika.imagetotext.ui.R
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LanguageModelSelect(
    languageModelSelected: (RecognationLanguageModel) -> Unit
) {

    Column(
        modifier = Modifier.background(Color.White)
    ) {
        ListItem {
            Text(stringResource(R.string.text_select_language))
        }
        RecognationLanguageModel.values().forEach { recogLangModel ->
            ListItem(
                modifier = Modifier.clickable {
                    languageModelSelected(recogLangModel)
                }
            ) {
                Text(recogLangModel.getText())
            }
        }
    }
}

@Composable
private fun RecognationLanguageModel.getText(): String {
    return stringResource(
        when (this) {
            RecognationLanguageModel.LATIN -> R.string.lang_latin
            RecognationLanguageModel.CHINESE -> R.string.lang_chinese
            RecognationLanguageModel.JAPANESE -> R.string.lang_japanese
            RecognationLanguageModel.KOREAN -> R.string.lang_korean
            RecognationLanguageModel.DEVANAGARI -> R.string.lang_devanagari
        }
    )
}

@Preview
@Composable
private fun LanguageModelSelectPreview() {
    AppTheme {
        LanguageModelSelect(
            languageModelSelected = {}
        )
    }
}