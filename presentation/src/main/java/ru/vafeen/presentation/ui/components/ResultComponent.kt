package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafeen.domain.models.QuizResult
import ru.vafeen.domain.network.repository.QuizNetworkRepository
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.screens.quiz_screen.QuizState
import ru.vafeen.presentation.ui.theme.StarTextColor

/**
 * Компонент отображения результата викторины с рейтингом, количеством правильных ответов,
 * описанием и кнопкой повторного запуска викторины.
 *
 * @param state Текущее состояние результата викторины.
 * @param onTryAgainClick Лямбда, вызываемая при нажатии кнопки "Попробовать снова".
 */
@Composable
internal fun ResultComponent(state: QuizState.Result, onTryAgainClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(QuizNetworkRepository.COUNT_OF_QUESTIONS_IN_ONE_QUIZ) { index ->
                    Image(
                        painter = painterResource(
                            if (index < state.quizResult.correctAnswers) R.drawable.filled_star
                            else R.drawable.star
                        ),
                        contentDescription = stringResource(R.string.star)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "${state.quizResult.correctAnswers} " +
                        stringResource(R.string.from) + " " +
                        QuizNetworkRepository.COUNT_OF_QUESTIONS_IN_ONE_QUIZ,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = StarTextColor
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(
                    id = when (state.quizResult) {
                        QuizResult.Score0Of5 -> R.string.rating_0_title
                        QuizResult.Score1Of5 -> R.string.rating_1_title
                        QuizResult.Score2Of5 -> R.string.rating_2_title
                        QuizResult.Score3Of5 -> R.string.rating_3_title
                        QuizResult.Score4Of5 -> R.string.rating_4_title
                        QuizResult.Score5Of5 -> R.string.rating_5_title
                    }
                ),
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(
                    id = when (state.quizResult) {
                        QuizResult.Score0Of5 -> R.string.rating_0_description
                        QuizResult.Score1Of5 -> R.string.rating_1_description
                        QuizResult.Score2Of5 -> R.string.rating_2_description
                        QuizResult.Score3Of5 -> R.string.rating_3_description
                        QuizResult.Score4Of5 -> R.string.rating_4_description
                        QuizResult.Score5Of5 -> R.string.rating_5_description
                    }
                ),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(64.dp))
            RounderCornerButton(onClick = onTryAgainClick) {
                Text(
                    text = stringResource(R.string.try_again).uppercase()
                )
            }
        }
    }
}
