package com.example.businessplanai.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnAccentButtonDark, // Текст на акцентных элементах
    background = BackgroundDark,
    surface = CardSurfaceDark,     // Фон карточек
    onBackground = CardTextDark,   // Основной текст
    onSurface = CardTextDark,      // Текст на поверхностях (карточках)
    primaryContainer = AccentButtonDark, // Акцентные кнопки
    onPrimaryContainer = OnAccentButtonDark, // Текст на акцентных кнопках
    secondaryContainer = SecondaryButtonDark, // Обычные кнопки
    onSecondaryContainer = OnSecondaryButtonDark // Текст на обычных кнопках
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnAccentButtonLight, // Текст на акцентных элементах
    background = BackgroundLight,
    surface = CardSurfaceLight,     // Фон карточек
    onBackground = CardTextLight,   // Основной текст
    onSurface = CardTextLight,      // Текст на поверхностях (карточках)
    primaryContainer = AccentButtonLight, // Акцентные кнопки
    onPrimaryContainer = OnAccentButtonLight, // Текст на акцентных кнопках
    secondaryContainer = SecondaryButtonLight, // Обычные кнопки
    onSecondaryContainer = OnSecondaryButtonLight // Текст на обычных кнопках
)

@Composable
fun BusinessPlanAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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
