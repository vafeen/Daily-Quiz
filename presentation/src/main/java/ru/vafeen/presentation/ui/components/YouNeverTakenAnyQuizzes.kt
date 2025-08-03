package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Компонент, отображаемый, когда пользователь ещё не проходил ни одной викторины.
 *
 * Отображает информационный текст и кнопку для начала новой викторины.
 *
 * @param modifier [Modifier] для настройки внешнего вида компонента.
 * @param onStartQuiz Лямбда, вызываемая при нажатии на кнопку или область компонента, для начала викторины.
 */
@Composable
internal fun YouNeverTakenAnyQuizzes(
    modifier: Modifier = Modifier,
    onStartQuiz: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(46.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onStartQuiz)
                .padding(24.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.you_never_taken_quizzes),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = AppTheme.colors.text
            )
            Spacer(modifier = Modifier.height(40.dp))
            RounderCornerButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onStartQuiz,
                containerColor = AppTheme.colors.background
            ) {
                Text(
                    text = stringResource(R.string.start_the_quiz),
                    fontSize = 16.sp,
                    color = AppTheme.colors.textOnBackground
                )
            }
        }
    }
}
