package com.priyanshu.floralens.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val PastelColorScheme = lightColorScheme(
    primary = LeafGreen,
    secondary = BotanicalGreen,
    tertiary = OliveGreen,
    background = PremiumWhite,
    surface = PureWhite,
    surfaceVariant = PastelGreenCard,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onTertiary = PureWhite,
    onBackground = TextDark,
    onSurface = TextDark,
    onSurfaceVariant = TextDark
)

@Composable
fun FloraLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Ignored to force bright botanical theme
    dynamicColor: Boolean = false, // Disabled dynamic color to force Botanical theme
    content: @Composable () -> Unit
) {
    // We enforce the bright pastel theme to maintain the "Digital Botanical Garden" aesthetic
    MaterialTheme(
        colorScheme = PastelColorScheme,
        typography = Typography,
        content = content
    )
}