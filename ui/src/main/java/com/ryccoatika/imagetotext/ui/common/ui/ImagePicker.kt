package com.ryccoatika.imagetotext.ui.common.ui

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.himanshoe.pluck.PluckConfiguration
import com.himanshoe.pluck.ui.Pluck
import com.himanshoe.pluck.ui.permission.Permission
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme

@Composable
fun ImagePicker() {
    Permission(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ),
        goToAppSettings = {
            Log.i("190401", "ImagePicker: goToAppSettings")
        },
    ) {
        Pluck(
            pluckConfiguration = PluckConfiguration(multipleImagesAllowed = false),
            onPhotoSelected = { pluckImages ->
                Log.i("190401", "ImagePicker: $pluckImages")
            },
        )
    }
}

@Preview
@Composable
private fun ImagePickerPreview() {
    AppTheme {
        ImagePicker()
    }
}