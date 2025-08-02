import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import ru.vafeen.presentation.ui.theme.White75
import ru.vafeen.presentation.ui.theme.White80
import ru.vafeen.presentation.ui.theme.White85
import ru.vafeen.presentation.ui.theme.White90
import ru.vafeen.presentation.ui.theme.White95
import kotlin.math.sin

/**
 * Круговой индикатор загрузки с градиентными полосками, расходящимися из центра.
 *
 * @property modifier Модификатор для настройки внешнего вида и поведения компонента
 * @property strokeWidth Толщина линий индикатора (по умолчанию 12 пикселей)
 * @property gradientColors Список цветов для градиента полосок (по умолчанию белые оттенки)
 * @property animationDuration Продолжительность полного цикла анимации в миллисекундах (по умолчанию 2000 мс)
 */
@Composable
internal fun RadialGradientLoader(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 12f,
    gradientColors: List<Color> = listOf(
        Color.White,
        White95,
        White90,
        White85,
        White80,
        White75
    ),
    animationDuration: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()

    /**
     * Анимация вращения индикатора (0-360 градусов)
     */
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    /**
     * Анимация прогресса градиента (0-1)
     */
    val gradientProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = modifier.size(72.dp)) {
        val center = size.center
        val radius = size.minDimension / 2 - strokeWidth
        val barLength = radius * 0.5f

        /**
         * Создает градиентную кисть для рисования полосок
         */
        val gradient = Brush.sweepGradient(
            colors = gradientColors,
            center = center
        )

        /**
         * Рисует 10 анимированных полосок с градиентом
         */
        repeat(10) { i ->
            val angle = i * 36f + rotationAngle

            rotate(angle, center) {
                val start = Offset(center.x, center.y - radius * 0.5f)
                val end = Offset(center.x, center.y - radius * 0.5f - barLength)

                drawLine(
                    brush = gradient,
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                    alpha = calculateLineAlpha(gradientProgress, i)
                )
            }
        }
    }
}

/**
 * Вычисляет прозрачность для отдельной полоски индикатора.
 *
 * @param progress Текущий прогресс анимации (0-1)
 * @param lineIndex Индекс полоски (0-9)
 * @return Значение прозрачности (0.7-1.0)
 */
private fun calculateLineAlpha(progress: Float, lineIndex: Int): Float =
    0.7f + 0.3f * sin((progress * 2 * Math.PI + lineIndex * 0.2f).toFloat())