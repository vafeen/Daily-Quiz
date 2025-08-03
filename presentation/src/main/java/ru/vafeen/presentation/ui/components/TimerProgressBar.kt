package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

/**
 * Горизонтальный прогресс-бар таймера, представляющий прогресс с помощью двух линий:
 * нижняя — серая фоновая, верхняя — цветная, длина которой пропорциональна прогрессу.
 *
 * @param modifier Модификатор для настройки внешнего вида компонента.
 * @param progress Прогресс отображения таймера, от 0.0 до 1.0 (0% - 100%).
 * @param color Цвет цветной линии прогресса.
 */
@Composable
fun TimerProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    color: Color,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
    ) {
        val width = size.width
        val centerY = size.height / 2
        val stroke = 6.dp.toPx()

        // Рисуем серую фоновую линию по всей ширине с закругленными краями
        drawLine(
            color = Color.Gray.copy(alpha = 0.5f),
            start = Offset(0f, centerY),
            end = Offset(width, centerY),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        // Рисуем цветную линию с длиной, пропорциональной значению прогресса
        drawLine(
            color = color,
            start = Offset(0f, centerY),
            end = Offset(width * progress.coerceIn(0f, 1f), centerY),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}
