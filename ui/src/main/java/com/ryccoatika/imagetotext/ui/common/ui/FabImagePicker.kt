package com.ryccoatika.imagetotext.ui.common.ui

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ryccoatika.imagetotext.ui.common.theme.spacing

@OptIn(ExperimentalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun FabImagePicker(
    pickedFromGallery: (Uri?) -> Unit,
    pickedFromCamera: (Uri?) -> Unit,
    generateImageUri: (Context) -> Uri
) {
    val context = LocalContext.current
    var fabAddActive by remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            pickedFromGallery(uri)
        }

    var imageUriFromCamera by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        if (result) {
            pickedFromCamera(imageUriFromCamera)
        } else {
            pickedFromCamera(null)
        }
    }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        AnimatedVisibility(
            visible = fabAddActive,
            enter = scaleIn() + slideInVertically { fullHeight ->
                fullHeight*2
            },
            exit = scaleOut() + slideOutVertically { fullHeight ->
                fullHeight*2
            }
        ) {
            FloatingActionButton(
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
                    imageVector = Icons.Default.Camera,
                    contentDescription = null,
                )
            }
        }
        AnimatedVisibility(
            visible = fabAddActive,
            enter = scaleIn() + slideInVertically { fullHeight ->
                fullHeight*2
            },
            exit = scaleOut() + slideOutVertically { fullHeight ->
                fullHeight*2
            }
        ) {
            FloatingActionButton(
                onClick = {
                    fabAddActive = false
                    galleryLauncher.launch("image/*")
                },
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                )
            }
        }
        FloatingActionButton(
            onClick = {
                fabAddActive = !fabAddActive
            },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        }
    }
}