package com.priyanshu.floralens.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Park
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.priyanshu.floralens.data.ScanResult
import com.priyanshu.floralens.ui.theme.BotanicalGreen
import com.priyanshu.floralens.ui.theme.LeafGreen
import com.priyanshu.floralens.ui.theme.OliveGreen
import com.priyanshu.floralens.ui.theme.PastelGreenCard
import com.priyanshu.floralens.ui.theme.PastelPink
import com.priyanshu.floralens.ui.theme.PastelYellow
import com.priyanshu.floralens.ui.theme.PremiumWhite
import com.priyanshu.floralens.ui.theme.TextDark

@Composable
fun VineSnackbar(
    scanResult: ScanResult?,
    errorMessage: String?,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(bottom = 56.dp) // Leave space for bottom nav
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Overlapping vines/decorations behind the box but peeking out
            Icon(
                imageVector = Icons.Filled.Park,
                contentDescription = null,
                tint = OliveGreen.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(60.dp)
                    .offset(x = (-20).dp, y = (-20).dp)
                    .rotate(-15f)
                    .align(Alignment.TopStart)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.LocalFlorist,
                contentDescription = null,
                tint = PastelPink,
                modifier = Modifier
                    .size(40.dp)
                    .offset(x = 10.dp, y = (-25).dp)
                    .rotate(20f)
                    .align(Alignment.TopEnd)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = PastelYellow,
                modifier = Modifier
                    .size(48.dp)
                    .offset(x = 20.dp, y = 20.dp)
                    .rotate(130f)
                    .align(Alignment.BottomEnd)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = LeafGreen.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(50.dp)
                    .offset(x = (-15).dp, y = 30.dp)
                    .rotate(-45f)
                    .align(Alignment.BottomStart)
                    .zIndex(2f)
            )

            // Main Content Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f)
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(PremiumWhite)
                    .border(3.dp, BotanicalGreen, RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                if (scanResult != null) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = scanResult.diseaseName,
                            color = LeafGreen,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(PastelGreenCard)
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Cause",
                                    color = TextDark,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = scanResult.cause,
                                    color = TextDark,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(PastelYellow.copy(alpha = 0.3f))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Treatment",
                                    color = TextDark,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = scanResult.treatment,
                                    color = TextDark,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = TextDark,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

