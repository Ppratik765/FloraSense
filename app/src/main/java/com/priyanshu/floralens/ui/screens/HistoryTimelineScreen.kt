package com.priyanshu.floralens.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import android.view.SoundEffectConstants
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.priyanshu.floralens.data.PlantProfile
import com.priyanshu.floralens.ui.components.BotanicalBackgroundDecorations
import com.priyanshu.floralens.ui.theme.*
import com.priyanshu.floralens.viewmodel.MainViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryTimelineScreen(viewModel: MainViewModel, onPlantClick: (String) -> Unit) {
    val profiles by viewModel.plantProfiles.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        BotanicalBackgroundDecorations()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.History,
                    contentDescription = null,
                    tint = FloraVibrant,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Plant Timeline",
                    style = MaterialTheme.typography.headlineLarge,
                    color = FloraVibrant,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (profiles.isEmpty()) "" else "${profiles.size} plants tracked",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (profiles.isEmpty()) {
                EmptyTimelineState()
            } else {
                // Timeline LazyRow
                val sortedProfiles = profiles.sortedByDescending { it.latestTimestamp }
                val timelineColor = FloraVibrant

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            // Draw continuous horizontal timeline stroke
                            val lineY = 80.dp.toPx() // Center of the thumbnails area
                            drawLine(
                                color = timelineColor.copy(alpha = 0.4f),
                                start = Offset(0f, lineY),
                                end = Offset(size.width, lineY),
                                strokeWidth = 3.dp.toPx()
                            )
                        }
                ) {
                    itemsIndexed(sortedProfiles) { index, profile ->
                        TimelineNode(
                            profile = profile,
                            onClick = { onPlantClick(profile.plantId) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Summary cards below the timeline
                Text(
                    text = "Tap a plant to view full history",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
fun TimelineNode(profile: PlantProfile, onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current
    val sortedScans = profile.scans.sortedByDescending { it.timestamp }
    val dateFormat = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(140.dp)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }
    ) {
        // Stacked image thumbnails
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Show up to 3 stacked thumbnails
            val displayScans = sortedScans.take(3).reversed()
            displayScans.forEachIndexed { index, scan ->
                val offsetX = (index * 8).dp
                val offsetY = -(index * 8).dp
                val imgSize = (90 - index * 6).dp

                Box(
                    modifier = Modifier
                        .offset(x = offsetX, y = offsetY)
                        .size(imgSize)
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurface)
                        .border(2.dp, if (index == displayScans.lastIndex) FloraVibrant else CardBorder, RoundedCornerShape(16.dp))
                ) {
                    if (scan.imagePath.isNotEmpty() && File(scan.imagePath).exists()) {
                        val bitmap = remember(scan.imagePath) {
                            BitmapFactory.decodeFile(scan.imagePath)
                        }
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = scan.diseaseName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            PlaceholderThumbnail()
                        }
                    } else {
                        PlaceholderThumbnail()
                    }
                }
            }

            // Scan count badge
            if (sortedScans.size > 1) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 8.dp, y = (-8).dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(FloraVibrant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${sortedScans.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = DeepForest,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Timeline dot
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(FloraVibrant)
                .border(2.dp, FloraLight, CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Plant name
        Text(
            text = profile.customName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // Latest status
        val latestScan = sortedScans.firstOrNull()
        if (latestScan != null) {
            Text(
                text = latestScan.diseaseName,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = dateFormat.format(Date(latestScan.timestamp)),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PlaceholderThumbnail() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CardBorder),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Eco,
            contentDescription = null,
            tint = FloraVibrant.copy(alpha = 0.4f),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun EmptyTimelineState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.LocalFlorist,
            contentDescription = null,
            tint = FloraVibrant.copy(alpha = 0.5f),
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Your plant timeline is empty.",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Scan your first plant to start tracking.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
