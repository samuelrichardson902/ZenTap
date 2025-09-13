package com.zentap.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Modern Light Theme Color Scheme
 * Primary: Medium Blue
 * Secondary: Neutral Gray
 * Background: Off-white for a softer look than pure white.
 */
private val LightColors = lightColorScheme(
    primary = Color(0xFF3B82F6),     // Medium Blue (Vibrant and accessible)
    onPrimary = Color(0xFFFFFFFF),   // White text on primary blue elements

    secondary = Color(0xFF6B7280), // Neutral Gray (for less important elements)
    onSecondary = Color(0xFFFFFFFF), // White text on secondary gray elements

    tertiary = Color(0xFF14B8A6),    // Teal accent color
    onTertiary = Color(0xFFFFFFFF),  // White text on tertiary elements

    background = Color(0xFFF9FAFB),  // Very light gray background (softer than pure white)
    onBackground = Color(0xFF1F2937), // Dark gray text for readability

    surface = Color(0xFFFFFFFF),     // White surface for cards, dialogs, and sheets
    onSurface = Color(0xFF1F2937),   // Dark gray text on surface elements

    primaryContainer = Color(0xFFDBEAFE),
    onPrimaryContainer = Color(0xFF1E40AF),

    error = Color(0xFFEF3F3F),             // A strong, clear red for errors
    onError = Color(0xFFFFFFFF),           // White text on error color
    errorContainer = Color(0xFFFFCDD2),    // A light red background for error components
    onErrorContainer = Color(0xFFB71C1C)
)

/**
 * Modern Dark Theme Color Scheme
 * Colors are lightened to provide sufficient contrast against the dark background.
 * Background: Very dark blue/gray instead of pure black for reduced eye strain.
 */
private val DarkColors = darkColorScheme(
    primary = Color(0xFF60A5FA),     // Lighter Blue (for good contrast on dark background)
    onPrimary = Color(0xFF1E3A8A),   // Dark blue text for contrast on light blue button

    secondary = Color(0xFF9CA3AF), // Lighter Gray
    onSecondary = Color(0xFF374151), // Dark gray text on light gray elements

    tertiary = Color(0xFF2DD4BF),    // Lighter Teal accent
    onTertiary = Color(0xFF047857),  // Dark teal text on light teal elements

    background = Color(0xFF111827),  // Very dark blue-gray background
    onBackground = Color(0xFFE5E7EB), // Light gray text for readability

    surface = Color(0xFF1F2937),
    onSurface = Color(0xFFF3F4F6),

    primaryContainer = Color(0xFF1E40AF),
    onPrimaryContainer = Color(0xFFDBEAFE),

    error = Color(0xFFF13232),             // A brighter red for visibility on dark backgrounds
    onError = Color(0xFF000000),           // High-contrast dark text on error color
    errorContainer = Color(0xFF6E0003),    // A muted, dark red background
    onErrorContainer = Color(0xFFF2B8B5)
)

@Composable
fun ZenTapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}