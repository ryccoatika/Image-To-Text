package com.ryccoatika.imagetotext.ui.common.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LinkedCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ryccoatika.imagetotext.ui.common.theme.AppTheme
import com.ryccoatika.imagetotext.ui.common.theme.spacing

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FabImagePicker(
    pickedFromGallery: (Uri) -> Unit,
    pickedFromCamera: (Uri) -> Unit,
    generateImageUri: (Context) -> Uri,
) {
    val context = LocalContext.current
    var fabAddActive by remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                pickedFromGallery(uri)
            }
        }

    var imageUriFromCamera by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                imageUriFromCamera?.let { pickedFromCamera(it) }
            }
        }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA) { granted ->
        if (granted) {
            imageUriFromCamera = generateImageUri(context)
            cameraLauncher.launch(imageUriFromCamera)
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLarge),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(
            visible = fabAddActive,
            enter = scaleIn() + slideInHorizontally { fullWidth ->
                fullWidth
            },
            exit = scaleOut() + slideOutHorizontally { fullWidth ->
                fullWidth
            },
        ) {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary,
                modifier = Modifier.size(40.dp),
                onClick = {
                    fabAddActive = false
                    galleryLauncher.launch("image/*")
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                )
            }
        }
        val rotation = remember { Animatable(0f) }
        FloatingActionButton(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            onClick = {
                fabAddActive = !fabAddActive
            },
        ) {
            LaunchedEffect(fabAddActive) {
                if (fabAddActive) {
                    rotation.animateTo(45f)
                } else {
                    rotation.animateTo(0f)
                }
            }
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.rotate(rotation.value),
            )
        }
        AnimatedVisibility(
            visible = fabAddActive,
            enter = scaleIn() + slideInHorizontally { fullWidth ->
                -fullWidth
            },
            exit = scaleOut() + slideOutHorizontally { fullWidth ->
                -fullWidth
            },
        ) {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.secondary,
                contentColor = MaterialTheme.colors.onSecondary,
                modifier = Modifier.size(40.dp),
                onClick = {
                    fabAddActive = false
                    if (cameraPermissionState.status.isGranted) {
                        imageUriFromCamera = generateImageUri(context)
                        cameraLauncher.launch(imageUriFromCamera)
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Outlined.LinkedCamera,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FabImagePicker() {
    AppTheme {
        FabImagePicker(
            pickedFromGallery = {},
            pickedFromCamera = {},
            generateImageUri = { Uri.EMPTY },
        )
    }
}
