package com.priyanshu.floralens.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.priyanshu.floralens.ui.components.BotanicalBackgroundDecorations
import com.priyanshu.floralens.ui.components.CameraPreview
import com.priyanshu.floralens.ui.components.VineSnackbar
import com.priyanshu.floralens.ui.theme.BotanicalGreen
import com.priyanshu.floralens.ui.theme.PureWhite
import com.priyanshu.floralens.viewmodel.AppState
import com.priyanshu.floralens.viewmodel.MainViewModel

@Composable
fun ScanScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val appState by viewModel.appState.collectAsState()
    var latestBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (hasCameraPermission && appState !is AppState.Result) {
                DiagnoseFAB(
                    isAnalyzing = appState is AppState.Analyzing,
                    onClick = {
                        latestBitmap?.let { viewModel.analyzeImage(it) }
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            BotanicalBackgroundDecorations()

            if (hasCameraPermission) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    CameraPreview(
                        onImageCaptured = { bitmap ->
                            latestBitmap = bitmap
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Camera permission is required.",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Upgraded VineSnackbar
            val showSnackbar = appState is AppState.Result || appState is AppState.Error
            
            val errorMessage = when (val state = appState) {
                is AppState.Result -> if (!state.classification.isPlantDetected) state.classification.diseaseName else null
                is AppState.Error -> state.message
                else -> null
            }
            
            val scanResult = when (val state = appState) {
                is AppState.Result -> state.scanResult
                else -> null
            }

            VineSnackbar(
                scanResult = scanResult,
                errorMessage = errorMessage,
                isVisible = showSnackbar,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            
            // If the snackbar is shown, clicking anywhere else should dismiss it
            if (showSnackbar) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { viewModel.dismissResult() }
                )
            }
        }
    }
}

@Composable
fun DiagnoseFAB(isAnalyzing: Boolean, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "FAB pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isAnalyzing) 1.1f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    FloatingActionButton(
        onClick = onClick,
        containerColor = BotanicalGreen,
        contentColor = PureWhite,
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = "Diagnose"
        )
    }
}
