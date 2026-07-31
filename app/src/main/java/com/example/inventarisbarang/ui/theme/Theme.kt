package com.example.inventarisbarang.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Definisi warna untuk tema aplikasi.
 *
 * Material3 menggunakan sistem "color roles" dimana setiap warna
 * memiliki peran tertentu (primary, secondary, surface, error, dll).
 *
 * Warna utama menggunakan ungu (Purple) sesuai mockup.
 */

// Warna untuk Light Theme (Emerald / Teal)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A60),             // Emerald Green
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF74F8E5),
    onPrimaryContainer = Color(0xFF00201C),
    secondary = Color(0xFF4A635F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8E2),
    onSecondaryContainer = Color(0xFF05201C),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    surface = Color(0xFFFAFDFB),
    onSurface = Color(0xFF191C1B),
    surfaceVariant = Color(0xFFDAE5E1),
    onSurfaceVariant = Color(0xFF3F4946),
    outline = Color(0xFF6F7976)
)

// Warna untuk Dark Theme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF53DBC9),             // Teal terang
    onPrimary = Color(0xFF003731),
    primaryContainer = Color(0xFF005048),
    onPrimaryContainer = Color(0xFF74F8E5),
    secondary = Color(0xFFB1CCC6),
    onSecondary = Color(0xFF1C3531),
    secondaryContainer = Color(0xFF334B47),
    onSecondaryContainer = Color(0xFFCCE8E2),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    surface = Color(0xFF191C1B),
    onSurface = Color(0xFFE0E3E1),
    surfaceVariant = Color(0xFF3F4946),
    onSurfaceVariant = Color(0xFFBEC9C6),
    outline = Color(0xFF899390)
)

/**
 * Tema utama aplikasi Inventaris Barang.
 *
 * Menggunakan Dynamic Color (Material You) pada Android 12+ (API 31+),
 * yang secara otomatis menyesuaikan warna app dengan wallpaper user.
 *
 * Pada Android 11 ke bawah, menggunakan warna statis yang sudah didefinisikan di atas.
 *
 * @param darkTheme Apakah menggunakan tema gelap (mengikuti setting sistem)
 * @param dynamicColor Apakah menggunakan Dynamic Color (default: true)
 * @param content Konten Composable yang dibungkus tema ini
 */
@Composable
fun StockFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Dynamic Color hanya tersedia di Android 12+ (API 31+)
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
