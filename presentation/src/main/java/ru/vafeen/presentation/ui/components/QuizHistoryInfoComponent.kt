package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafeen.domain.models.QuizSessionResultPreview
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Компонент для отображения краткой информации о сессии викторины в списке истории.
 *
 * В качестве расширения на [QuizSessionResultPreview], отображает имя сессии, количество правильных ответов
 * в виде звёзд, а также дату и время сессии.
 *
 * @receiver [QuizSessionResultPreview] - данные сессии, которые отображаются.
 * @param modifier [Modifier] для настройки внешнего вида компонента.
 * @param onClick Лямбда, вызываемая при нажатии на карточку сессии.
 */
@Composable
internal fun QuizSessionResultPreview.QuizHistoryInfoComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    fontSize = 24.sp,
                    color = AppTheme.colors.quizNameText,
                    style = MaterialTheme.typography.titleMedium
                )
                Stars(
                    modifier = Modifier.width(112.dp),
                    countOfFilledStars = countOfRightAnswers,
                    size = 16.dp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = date,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.text
                )
                Text(
                    text = time,
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.colors.text
                )
            }
        }
    }
}
