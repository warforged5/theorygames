package io.github.warforged5.theorygames.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Classic Purple Theme (Original)
private val ClassicLightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

private val ClassicDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

// Ocean Blue Theme
private val OceanLightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),
    secondary = Color(0xFF0288D1),
    tertiary = Color(0xFF00ACC1),
    primaryContainer = Color(0xFFE3F2FD),
    secondaryContainer = Color(0xFFE1F5FE),
    tertiaryContainer = Color(0xFFE0F7FA),
    background = Color(0xFFFAFCFF),
    surface = Color(0xFFFAFCFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onPrimaryContainer = Color(0xFF0D47A1),
    onSecondaryContainer = Color(0xFF01579B),
    onTertiaryContainer = Color(0xFF006064),
    onBackground = Color(0xFF1A1C1E),
    onSurface = Color(0xFF1A1C1E),
)

private val OceanDarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    secondary = Color(0xFF81D4FA),
    tertiary = Color(0xFF80DEEA),
    primaryContainer = Color(0xFF0D47A1),
    secondaryContainer = Color(0xFF01579B),
    tertiaryContainer = Color(0xFF006064),
    background = Color(0xFF0E1419),
    surface = Color(0xFF0E1419),
    onPrimary = Color(0xFF003366),
    onSecondary = Color(0xFF014478),
    onTertiary = Color(0xFF004D51)
)

// Forest Green Theme
private val ForestLightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32),
    secondary = Color(0xFF388E3C),
    tertiary = Color(0xFF4CAF50),
    primaryContainer = Color(0xFFE8F5E8),
    secondaryContainer = Color(0xFFE8F5E8),
    tertiaryContainer = Color(0xFFF1F8E9),
    background = Color(0xFFFAFDFA),
    surface = Color(0xFFFAFDFA),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onPrimaryContainer = Color(0xFF1B5E20),
    onSecondaryContainer = Color(0xFF1B5E20),
    onTertiaryContainer = Color(0xFF33691E),
    onBackground = Color(0xFF1A1C1A),
    onSurface = Color(0xFF1A1C1A),
)

private val ForestDarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784),
    secondary = Color(0xFF4CAF50),
    tertiary = Color(0xFF8BC34A),
    primaryContainer = Color(0xFF1B5E20),
    secondaryContainer = Color(0xFF2E7D32),
    tertiaryContainer = Color(0xFF33691E),
    background = Color(0xFF0F1B0F),
    surface = Color(0xFF0F1B0F),
    onPrimary = Color(0xFF003300),
    onSecondary = Color(0xFF1B4E1F),
    onTertiary = Color(0xFF1A3A1A)
)

// Sunset Orange Theme
private val SunsetLightColorScheme = lightColorScheme(
    primary = Color(0xFFE65100),
    secondary = Color(0xFFFF5722),
    tertiary = Color(0xFFFF9800),
    primaryContainer = Color(0xFFFFF3E0),
    secondaryContainer = Color(0xFFFBE9E7),
    tertiaryContainer = Color(0xFFFFF8E1),
    background = Color(0xFFFFFBF7),
    surface = Color(0xFFFFFBF7),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onPrimaryContainer = Color(0xFFBF360C),
    onSecondaryContainer = Color(0xFFD84315),
    onTertiaryContainer = Color(0xFFE65100),
    onBackground = Color(0xFF1F1B16),
    onSurface = Color(0xFF1F1B16),
)

private val SunsetDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFAB40),
    secondary = Color(0xFFFF7043),
    tertiary = Color(0xFFFFB74D),
    primaryContainer = Color(0xFFBF360C),
    secondaryContainer = Color(0xFFD84315),
    tertiaryContainer = Color(0xFFE65100),
    background = Color(0xFF1A1511),
    surface = Color(0xFF1A1511),
    onPrimary = Color(0xFF662200),
    onSecondary = Color(0xFF5D1A00),
    onTertiary = Color(0xFF663300)
)

// Midnight Theme (Always Dark)
private val MidnightColorScheme = darkColorScheme(
    primary = Color(0xFFCE93D8),
    secondary = Color(0xFFB39DDB),
    tertiary = Color(0xFF9FA8DA),
    primaryContainer = Color(0xFF4A148C),
    secondaryContainer = Color(0xFF311B92),
    tertiaryContainer = Color(0xFF1A237E),
    background = Color(0xFF0A0A0F),
    surface = Color(0xFF121218),
    surfaceContainer = Color(0xFF1A1A20),
    surfaceContainerHigh = Color(0xFF222228),
    onPrimary = Color(0xFF2E0066),
    onSecondary = Color(0xFF1A0D66),
    onTertiary = Color(0xFF0D1966),
    onBackground = Color(0xFFE4E1E6),
    onSurface = Color(0xFFE4E1E6),
    outline = Color(0xFF938F96),
    outlineVariant = Color(0xFF48464F),
)

@Composable
fun TheoryGamesTheme(
    selectedTheme: String = "SYSTEM",
    isDarkMode: Boolean? = null,
    content: @Composable () -> Unit
) {
    val systemDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current

    // Determine dark theme preference
    val isDarkTheme = when {
        selectedTheme == "MIDNIGHT" -> true // Midnight is always dark
        isDarkMode != null -> isDarkMode // User preference
        else -> systemDarkTheme // Follow system
    }

    // Select color scheme based on theme and dark mode
    val colorScheme = when (selectedTheme) {
        "DYNAMIC" -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (isDarkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            } else {
                // Fallback to classic theme on older Android versions
                if (isDarkTheme) ClassicDarkColorScheme else ClassicLightColorScheme
            }
        }
        "OCEAN" -> {
            if (isDarkTheme) OceanDarkColorScheme else OceanLightColorScheme
        }
        "FOREST" -> {
            if (isDarkTheme) ForestDarkColorScheme else ForestLightColorScheme
        }
        "SUNSET" -> {
            if (isDarkTheme) SunsetDarkColorScheme else SunsetLightColorScheme
        }
        "MIDNIGHT" -> {
            MidnightColorScheme // Always dark
        }
        "CLASSIC" -> {
            if (isDarkTheme) ClassicDarkColorScheme else ClassicLightColorScheme
        }
        else -> {
            // Default to SYSTEM behavior (Classic theme following system)
            if (isDarkTheme) ClassicDarkColorScheme else ClassicLightColorScheme
        }
    }

    // Update system bars
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}