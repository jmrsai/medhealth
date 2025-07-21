package com.jmr.medhealth.ui.capture

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.jmr.medhealth.util.TFLiteHelper
import com.jmr.medhealth.util.toBitmap

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CaptureScreen(
    viewModel: CaptureViewModel = hiltViewModel(),
    navigateToDetails: (Int) -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val imageCapture = remember { ImageCapture.Builder().build() }
    val captureState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle navigation and error messages
    LaunchedEffect(captureState) {
        when (val state = captureState) {
            is CaptureUiState.Success -> {
                navigateToDetails(state.diagnosisId)
                viewModel.resetState() // Reset state after navigation
            }
            is CaptureUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState() // Reset state after showing error
            }
            else -> { /* Do nothing for Idle or Loading */ }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        if (cameraPermissionState.status.isGranted) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    imageCapture = imageCapture
                )
                if (captureState is CaptureUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    FloatingActionButton(
                        onClick = {
                            captureAndAnalyzeImage(context, imageCapture, viewModel)
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(32.dp)
                            .size(72.dp)
                    ) {
                        Icon(Icons.Default.Camera, contentDescription = "Capture", modifier = Modifier.size(36.dp))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Camera permission is required for AI diagnosis.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request Permission")
                }
            }
        }
    }
}

private fun captureAndAnalyzeImage(
    context: Context,
    imageCapture: ImageCapture,
    viewModel: CaptureViewModel
) {
    val executor = ContextCompat.getMainExecutor(context)
    imageCapture.takePicture(executor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val bitmap = image.toBitmap()
            val rotationDegrees = image.imageInfo.rotationDegrees
            val rotatedBitmap = if (rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }

            viewModel.analyzeImage(rotatedBitmap, TFLiteHelper(context))
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            // You can show a snackbar or log the error
            viewModel.resetState() // Ensure loading stops on error
        }
    })
}