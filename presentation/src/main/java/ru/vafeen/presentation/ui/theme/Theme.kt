package ru.vafeen.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


internal data class AppThemeColors(
    val background: Color,
    val cardBackground: Color,
    val text: Color,
    val questionNumberColor: Color, // текст для Вопрос 1 из 5
    val quizNameText: Color,
    val star: Color,
    val errorText: Color,
    val textOnBackground: Color,
)

private val basePalette = AppThemeColors(
    background = VioletBlue,
    cardBackground = Color.White,
    text = Color.Black,
    questionNumberColor = QuestionNumberColor,
    quizNameText = DarkBlue,
    star = StarTextColor,
    errorText = Color.Red,
    textOnBackground = Color.White,
)

private val baseDarkPalette = basePalette.copy(
    cardBackground = Color.Black,
    text = Color.White,
)


@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colors = if (darkTheme) {
        baseDarkPalette
    } else {
        basePalette
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}

internal object AppTheme {
    val colors: AppThemeColors
        @Composable
        get() = LocalColors.current
}

private val LocalColors = staticCompositionLocalOf<AppThemeColors> {
    error("Composition error")
}