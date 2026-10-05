package com.example.calculator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// A neutral gray palette rather than Material's default purple, so the
// calculator's accent colors stay monochrome in both light and dark mode.
private val DarkColorScheme = darkColorScheme(
    primary = Gray80,
    onPrimary = Gray10,
    secondary = Gray70,
    onSecondary = Gray10,
    secondaryContainer = Gray25,
    onSecondaryContainer = Gray90,
    tertiary = Gray70,
    onTertiary = Gray10,
    tertiaryContainer = Gray35,
    onTertiaryContainer = Gray95,
    background = Gray05,
    onBackground = Gray95,
    surface = Gray05,
    onSurface = Gray95,
    surfaceVariant = Gray20,
    onSurfaceVariant = Gray90,
    outlineVariant = Gray40
)

private val LightColorScheme = lightColorScheme(
    primary = Gray20,
    onPrimary = Gray99,
    secondary = Gray40,
    onSecondary = Gray99,
    secondaryContainer = Gray90,
    onSecondaryContainer = Gray10,
    tertiary = Gray40,
    onTertiary = Gray99,
    tertiaryContainer = Gray85,
    onTertiaryContainer = Gray10,
    background = Gray98,
    onBackground = Gray10,
    surface = Gray98,
    onSurface = Gray10,
    surfaceVariant = Gray95,
    onSurfaceVariant = Gray10,
    outlineVariant = Gray70
)

@Composable
fun CalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color (Material You) would pull accent colors from the
    // device wallpaper; disabled so the app keeps its own gray palette.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}