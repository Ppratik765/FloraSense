package com.priyanshu.floralens.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import android.view.SoundEffectConstants
import com.priyanshu.floralens.ui.theme.FloraTheme
@Composable
fun ThemeToggleButton(
    isLightMode: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val view = LocalView.current

    val rotation by animateFloatAsState(
        targetValue = if (isLightMode) 180f else 0f,
        animationSpec = tween(800),
        label = "ThemeRotation"
    )

    Box(
        modifier = modifier
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(FloraTheme.colors.cardSurface)
            .border(2.dp, FloraTheme.colors.cardBorder, CircleShape)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onToggle()
            }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isLightMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
            contentDescription = "Toggle Theme",
            tint = FloraTheme.colors.floraVibrant,
            modifier = Modifier
                .size(24.dp)
                .rotate(rotation)
        )
    }
}
