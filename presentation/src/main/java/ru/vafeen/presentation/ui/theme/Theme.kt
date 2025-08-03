package ru.vafeen.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Набор цветов для темы приложения.
 *
 * @property background Основной цвет фона.
 * @property cardBackground Цвет фона карточек.
 * @property cardOnCardBackground Цвет фона вложенных карточек.
 * @property text Цвет основного текста.
 * @property questionNumberColor Цвет текста из строки "Вопрос 1 из 5".
 * @property quizNameText Цвет текста названия викторины и связанных элементов.
 * @property star Цвет звездочек (рейтинга).
 * @property errorText Цвет текста ошибок.
 * @property textOnBackground Цвет текста поверх основного фона.
 */
internal data class AppThemeColors(
    val background: Color,
    val cardBackground: Color,
    val cardOnCardBackground: Color,
    val text: Color,
    val questionNumberColor: Color,
    val quizNameText: Color,
    val star: Color,
    val errorText: Color,
    val textOnBackground: Color,
)

private val basePalette = AppThemeColors(
    background = VioletBlue,
    cardBackground = Color.White,
    cardOnCardBackground = Color.LightGray,
    text = Color.Black,
    questionNumberColor = QuestionNumberColor,
    quizNameText = DarkBlue,
    star = StarTextColor,
    errorText = Color.Red,
    textOnBackground = Color.White,
)

private val baseDarkPalette = basePalette.copy(
    cardBackground = Color(0xFF313131),
    cardOnCardBackground = Color(0xFF656565),
    quizNameText = VioletBlue,
    text = Color.White,
)

/**
 * Применяет тему приложения, автоматически выбирая светлую или тёмную цветовую палитру.
 *
 * @param darkTheme Флаг для использования тёмной темы. По умолчанию определяется системой.
 * @param content Контент, который будет обёрнут в заданную тему.
 */
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

/**
 * Объект для доступа к цветам темы из любого @Composable.
 */
internal object AppTheme {
    val colors: AppThemeColors
        @Composable
        get() = LocalColors.current
}

/**
 * CompositionLocal для передачи цветов темы в дерево компонентов.
 */
private val LocalColors = staticCompositionLocalOf<AppThemeColors> {
    error("Composition error")
}
