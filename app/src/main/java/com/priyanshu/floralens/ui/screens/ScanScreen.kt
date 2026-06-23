package com.priyanshu.floralens.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import android.view.SoundEffectConstants
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.priyanshu.floralens.ui.components.BotanicalBackgroundDecorations
import com.priyanshu.floralens.ui.components.CameraPreview
import com.priyanshu.floralens.ui.components.VineSnackbar
import com.priyanshu.floralens.ui.theme.*
import com.priyanshu.floralens.viewmodel.AppState
import com.priyanshu.floralens.viewmodel.MainViewModel

@Composable
fun ScanScreen(viewModel: MainViewModel, onScanSaved: () -> Unit = {}) {
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
    var isFlashlightOn by remember { mutableStateOf(false) }

    val view = LocalView.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            if (hasCameraPermission && appState !is AppState.Result && appState !is AppState.AwaitingPlantSelection) {
                DiagnoseFAB(
                    isAnalyzing = appState is AppState.Analyzing,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        view.playSoundEffect(SoundEffectConstants.CLICK)
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
                        .aspectRatio(1f)
                        .padding(16.dp)
                        .align(Alignment.Center)
                ) {
                    CameraPreview(
                        isFlashlightOn = isFlashlightOn,
                        onImageCaptured = { bitmap ->
                            latestBitmap = bitmap
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Flashlight toggle button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DeepForest.copy(alpha = 0.6f))
                            .clickable { isFlashlightOn = !isFlashlightOn }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Lightbulb,
                            contentDescription = "Toggle Flashlight",
                            tint = if (isFlashlightOn) FloraVibrant else PremiumWhite
                        )
                    }
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp, top = 24.dp)
                    .animateContentSize(animationSpec = tween(150)),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom)
            ) {
                // Plant Selection Overlay (Only takes as much height as it needs)
                if (appState is AppState.AwaitingPlantSelection) {
                    PlantSelectionOverlay(
                        viewModel = viewModel,
                        onSaved = onScanSaved,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // VineSnackbar (Takes remaining space and becomes scrollable)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    val showSnackbar = appState is AppState.Result || appState is AppState.Error || appState is AppState.AwaitingPlantSelection
                    val errorMessage = when (val state = appState) {
                        is AppState.Result -> if (state.scanResult == null) state.classification.diseaseName else null
                        is AppState.Error -> state.message
                        else -> null
                    }
                    val scanResult = when (val state = appState) {
                        is AppState.Result -> state.scanResult
                        is AppState.AwaitingPlantSelection -> state.scanResult
                        else -> null
                    }

                    VineSnackbar(
                        scanResult = scanResult,
                        errorMessage = errorMessage,
                        isVisible = showSnackbar,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }

            LaunchedEffect(appState) {
                when (appState) {
                    is AppState.Result, is AppState.Error, is AppState.AwaitingPlantSelection -> {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        view.playSoundEffect(SoundEffectConstants.NAVIGATION_UP)
                    }
                    is AppState.Idle -> {
                        view.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN)
                    }
                    else -> {}
                }
            }

            // Dismiss on tap ONLY if it's an error or a result without the plant selection overlay
            val isDismissibleOverlay = (appState is AppState.Result && appState !is AppState.AwaitingPlantSelection) || appState is AppState.Error
            if (isDismissibleOverlay) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) { viewModel.dismissResult() }
                )
            }

            // Intercept back presses to prevent accidental dismissal
            var backPressTime by remember { mutableStateOf(0L) }
            if (appState is AppState.AwaitingPlantSelection) {
                BackHandler {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - backPressTime < 2000) {
                        viewModel.dismissResult()
                    } else {
                        backPressTime = currentTime
                        Toast.makeText(context, "Press back again to discard diagnosis without saving", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun PlantSelectionOverlay(viewModel: MainViewModel, onSaved: () -> Unit = {}, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val profiles by viewModel.plantProfiles.collectAsState()
    var showNewPlantInput by remember { mutableStateOf(false) }
    var customName by remember { mutableStateOf("") }
    val pendingPlantId = viewModel.pendingPlantId

    // If we came from "Update Condition", skip the dialog and auto-add
    LaunchedEffect(pendingPlantId) {
        if (pendingPlantId != null) {
            viewModel.addToExistingPlant(context, pendingPlantId)
            onSaved()
        }
    }

    if (pendingPlantId != null) return // Auto-handled above

    androidx.compose.material3.Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .animateContentSize(animationSpec = tween(150)),
        shape = RoundedCornerShape(24.dp),
        color = FloraTheme.colors.cardSurface,
        border = androidx.compose.foundation.BorderStroke(2.dp, FloraTheme.colors.cardBorder),
        shadowElevation = 16.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Eco,
                    contentDescription = null,
                    tint = FloraTheme.colors.floraVibrant,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (showNewPlantInput) "Name Your Plant" else "Save Scan",
                    fontWeight = FontWeight.Bold,
                    color = FloraTheme.colors.floraVibrant,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (showNewPlantInput) {
                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("Plant Name", color = FloraTheme.colors.textSecondary) },
                    placeholder = { Text("e.g. Kitchen Tomato", color = FloraTheme.colors.textSecondary.copy(alpha = 0.5f)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FloraTheme.colors.floraVibrant,
                        unfocusedBorderColor = FloraTheme.colors.cardBorder,
                        focusedLabelColor = FloraTheme.colors.floraVibrant,
                        cursorColor = FloraTheme.colors.floraVibrant,
                        focusedTextColor = FloraTheme.colors.textPrimary,
                        unfocusedTextColor = FloraTheme.colors.textPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    OutlinedButton(
                        onClick = {
                            showNewPlantInput = false
                            customName = ""
                        },
                        border = androidx.compose.foundation.BorderStroke(1.dp, FloraTheme.colors.cardBorder),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FloraTheme.colors.textSecondary)
                    ) {
                        Text("Back")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (customName.isNotBlank()) {
                                viewModel.createNewPlant(context, customName.trim())
                                onSaved()
                            }
                        },
                        enabled = customName.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FloraTheme.colors.floraVibrant,
                            contentColor = FloraTheme.colors.deepForest
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // New plant button
                Button(
                    onClick = { showNewPlantInput = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FloraTheme.colors.floraVibrant,
                        contentColor = FloraTheme.colors.deepForest
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("New Plant", fontWeight = FontWeight.Bold)
                }

                // Existing plants
                if (profiles.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Or add to existing:",
                        style = MaterialTheme.typography.labelSmall,
                        color = FloraTheme.colors.textSecondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyColumn(
                        modifier = Modifier.height((profiles.size * 52).coerceAtMost(200).dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(profiles) { profile ->
                            OutlinedButton(
                                onClick = {
                                    viewModel.addToExistingPlant(context, profile.plantId)
                                    onSaved()
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = FloraTheme.colors.floraLight
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, FloraTheme.colors.cardBorder),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Eco,
                                    contentDescription = null,
                                    tint = FloraTheme.colors.floraVibrant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = profile.customName,
                                    modifier = Modifier.weight(1f),
                                    color = FloraTheme.colors.textPrimary
                                )
                                Text(
                                    text = "${profile.scans.size} scans",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = FloraTheme.colors.textSecondary
                                )
                            }
                        }
                    }
                }
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
        containerColor = FloraVibrant,
        contentColor = DeepForest,
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = Icons.Filled.CameraAlt,
            contentDescription = "Diagnose"
        )
    }
}
