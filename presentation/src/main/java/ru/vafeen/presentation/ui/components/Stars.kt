package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.network.repository.QuizNetworkRepository
import ru.vafeen.presentation.R

/**
 * Отображает горизонтальный ряд звезд, часть из которых заполнена.
 *
 * Используется для визуального представления количества правильных ответов или
 * других значений в диапазоне от 0 до общего количества вопросов в викторине.
 *
 * @param modifier модификатор для настройки внешнего вида компонента
 * @param countOfFilledStars количество заполненных (выделенных) звезд
 * @param size размер каждой звезды (по умолчанию 52.dp)
 */
@Composable
internal fun Stars(
    modifier: Modifier = Modifier,
    countOfFilledStars: Int,
    size: Dp = 52.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(QuizNetworkRepository.COUNT_OF_QUESTIONS_IN_ONE_QUIZ) { index ->
            Image(
                modifier = Modifier.size(size),
                painter = painterResource(
                    if (index < countOfFilledStars) R.drawable.filled_star
                    else R.drawable.star
                ),
                contentDescription = stringResource(R.string.star)
            )
        }
    }
}
