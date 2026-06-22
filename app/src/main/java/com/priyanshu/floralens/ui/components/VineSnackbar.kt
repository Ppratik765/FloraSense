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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.unit.em
import androidx.compose.ui.zIndex
import com.priyanshu.floralens.data.ScanResult
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import com.priyanshu.floralens.ui.theme.*

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
            .navigationBarsPadding() // Dynamically handles 3-button or swipe nav
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            val canvasLineColor = YellowGreen

            // 1. Organic Canvas Vine Branches wrapping the border
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .zIndex(0f)
            ) {
                // Drawing winding vine paths on the left and right sides
                val pathLeft = Path().apply {
                    moveTo(15.dp.toPx(), size.height - 15.dp.toPx())
                    quadraticTo(
                        -25.dp.toPx(), size.height * 0.5f,
                        25.dp.toPx(), 15.dp.toPx()
                    )
                }
                drawPath(
                    path = pathLeft,
                    color = canvasLineColor,
                    style = Stroke(width = 4.dp.toPx())
                )

                val pathRight = Path().apply {
                    moveTo(size.width - 15.dp.toPx(), 15.dp.toPx())
                    quadraticTo(
                        size.width + 25.dp.toPx(), size.height * 0.5f,
                        size.width - 25.dp.toPx(), size.height - 15.dp.toPx()
                    )
                }
                drawPath(
                    path = pathRight,
                    color = canvasLineColor,
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            // 2. Leaf and Flower Icons (more in amount, more pronounced, overlapping borders)
            // Top Left Corner Ornaments
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = YellowGreen,
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = (-16).dp, y = (-16).dp)
                    .rotate(-30f)
                    .align(Alignment.TopStart)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.LocalFlorist,
                contentDescription = null,
                tint = PastelPink,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = 12.dp, y = (-20).dp)
                    .rotate(15f)
                    .align(Alignment.TopStart)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = LightGreen,
                modifier = Modifier
                    .size(22.dp)
                    .offset(x = (-22).dp, y = 20.dp)
                    .rotate(-80f)
                    .align(Alignment.TopStart)
                    .zIndex(2f)
            )

            // Top Right Corner Ornaments
            Icon(
                imageVector = Icons.Filled.LocalFlorist,
                contentDescription = null,
                tint = PastelPink,
                modifier = Modifier
                    .size(32.dp)
                    .offset(x = 14.dp, y = (-18).dp)
                    .rotate(45f)
                    .align(Alignment.TopEnd)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = LightGreen,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = (-16).dp, y = (-16).dp)
                    .rotate(45f)
                    .align(Alignment.TopEnd)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = YellowGreen,
                modifier = Modifier
                    .size(22.dp)
                    .offset(x = 20.dp, y = 24.dp)
                    .rotate(105f)
                    .align(Alignment.TopEnd)
                    .zIndex(2f)
            )

            // Bottom Left Corner Ornaments
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = LeafGreen.copy(alpha = 0.9f),
                modifier = Modifier
                    .size(38.dp)
                    .offset(x = (-18).dp, y = 18.dp)
                    .rotate(-45f)
                    .align(Alignment.BottomStart)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = YellowGreen,
                modifier = Modifier
                    .size(26.dp)
                    .offset(x = (-24).dp, y = (-12).dp)
                    .rotate(45f)
                    .align(Alignment.BottomStart)
                    .zIndex(2f)
            )

            // Bottom Right Corner Ornaments
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = PastelYellow,
                modifier = Modifier
                    .size(40.dp)
                    .offset(x = 18.dp, y = 18.dp)
                    .rotate(130f)
                    .align(Alignment.BottomEnd)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.LocalFlorist,
                contentDescription = null,
                tint = PastelPink,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = 22.dp, y = (-12).dp)
                    .rotate(-20f)
                    .align(Alignment.BottomEnd)
                    .zIndex(2f)
            )

            // Center edges
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = LightGreen,
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = (-14).dp, y = 0.dp)
                    .rotate(-90f)
                    .align(Alignment.CenterStart)
                    .zIndex(2f)
            )
            Icon(
                imageVector = Icons.Filled.Eco,
                contentDescription = null,
                tint = YellowGreen,
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = 14.dp, y = 0.dp)
                    .rotate(90f)
                    .align(Alignment.CenterEnd)
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
                                BulletedText(
                                    text = scanResult.cause,
                                    color = TextDark,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 1.5.em
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
                                BulletedText(
                                    text = scanResult.treatment,
                                    color = TextDark,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 1.5.em
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

