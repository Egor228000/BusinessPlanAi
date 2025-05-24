package com.example.businessplanai.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    onPrimary = BackgroundDark, // Задний фон
    background = TextColorDark,// Teкст  и иконки
    surface = TextDescriptionColorDark,     //Вспомогающий текст
    onBackground = CardSurfaceDark,   // Фон карточек и TextFieldOutline
    onSurface = AccentButtonLightDark,      // Кнопки
)
private val LightColorScheme = lightColorScheme(
    onPrimary = BackgroundLight,
    background = TextColorLight,
    surface = TextDescriptionColorLight,
    onBackground = CardSurfaceLight,
    onSurface = AccentButtonLightLight,
)



private val DarkRedColorScheme = darkColorScheme(
    onPrimary = BackgroundDarkRed,
    background = TextColorDarkRed,
    surface = TextDescriptionColorDarkRed,
    onBackground = CardSurfaceDarkRed,
    onSurface = AccentButtonRed,
)
private val LightRedColorScheme = lightColorScheme(
    onPrimary = BackgroundLightRed,
    background = TextColorLightRed,
    surface = TextDescriptionColorLightRed,
    onBackground = CardSurfaceLightRed,
    onSurface = AccentButtonRed,
)

private val DarkGreenColorScheme = darkColorScheme(
    onPrimary = BackgroundDarkGreen,
    background = TextColorDarkGreen,
    surface = TextDescriptionColorDarkGreen,
    onBackground = CardSurfaceDarkGreen,
    onSurface = AccentButtonGreen,
)
private val LightGreenColorScheme = lightColorScheme(
    onPrimary = BackgroundLightGreen,
    background = TextColorLightGreen,
    surface = TextDescriptionColorLightGreen,
    onBackground = CardSurfaceLightGreen,
    onSurface = AccentButtonGreen,
)


@Composable
fun BusinessPlanAITheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> true
        else -> false
    }

    val colorScheme = when (appTheme) {
        AppTheme.SYSTEM -> if (darkTheme) DarkColorScheme else LightColorScheme
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.DARKRed -> DarkRedColorScheme
        AppTheme.LIGHTRed -> LightRedColorScheme
        AppTheme.DARKGreen -> DarkGreenColorScheme
        AppTheme.LIGHTGreen -> LightGreenColorScheme

    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK,
    LIGHTRed,
    DARKRed,
    LIGHTGreen,
    DARKGreen
}