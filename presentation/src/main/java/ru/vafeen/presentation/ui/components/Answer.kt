package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.components.AnswerState.Chosen
import ru.vafeen.presentation.ui.components.AnswerState.Correct
import ru.vafeen.presentation.ui.components.AnswerState.Free
import ru.vafeen.presentation.ui.components.AnswerState.Incorrect
import ru.vafeen.presentation.ui.theme.DarkBlue
import ru.vafeen.presentation.ui.theme.DarkGreen


/**
 * Компонент отдельного варианта ответа в вопросе викторины.
 *
 * Отображает карточку с иконкой, отражающей состояние ответа, и текстом варианта.
 * Карточка кликабельна, если передан обработчик нажатия [onClick].
 *
 * @param modifier Модификатор для дополнительного оформления компонента.
 * @param text Текст варианта ответа.
 * @param answerState Текущее состояние варианта ответа (свободен, выбран, правильный, неправильный).
 * @param onClick Обработчик клика по варианту ответа, либо null если клик не активен.
 */
@Composable
internal fun Answer(
    modifier: Modifier = Modifier,
    text: String,
    answerState: AnswerState,
    onClick: (() -> Unit)? = null
) {
    val borderWidth = 1.dp
    Card(
        shape = RoundedCornerShape(16.dp),
        border = when (answerState) {
            AnswerState.Free -> null
            AnswerState.Chosen -> BorderStroke(width = borderWidth, color = DarkBlue)
            AnswerState.Correct -> BorderStroke(width = borderWidth, color = DarkGreen)
            AnswerState.Incorrect -> BorderStroke(width = borderWidth, color = Color.Red)
        },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .let {
                    if (onClick != null) it.clickable(onClick = onClick) else it
                }
                .padding(16.dp)
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(
                    id = when (answerState) {
                        AnswerState.Free -> R.drawable.free_circle
                        AnswerState.Chosen -> R.drawable.chosen_circle
                        AnswerState.Correct -> R.drawable.correct_circle
                        AnswerState.Incorrect -> R.drawable.incorrect_circle
                    }
                ),
                contentDescription = stringResource(R.string.answer_state)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Перечисление состояний ответа в викторине.
 *
 * @property Free Ответ ещё не выбран пользователем.
 * @property Chosen Ответ выбран пользователем, но ещё не проверен.
 * @property Correct Ответ выбран и подтверждён как правильный.
 * @property Incorrect Ответ выбран и подтверждён как неправильный.
 */
internal enum class AnswerState {
    Free,
    Chosen,
    Correct,
    Incorrect
}