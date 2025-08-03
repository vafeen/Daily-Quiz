package ru.vafeen.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.presentation.R
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Компонент вопроса викторины с вариантами ответов и кнопкой подтверждения выбора.
 *
 * Отображает текущий вопрос викторины с номером, текстом и списком ответов с их текущими состояниями.
 * Также предоставляет кнопку для подтверждения выбранного ответа, если это предусмотрено логикой.
 *
 * @param currentQuestion Текущий объект вопроса викторины, содержащий текст вопроса, ответы,
 * выбранный и правильный ответы.
 * @param numberOfCurrentQuestion Номер текущего вопроса в серии викторины.
 * @param quantityOfQuestions Общее количество вопросов в викторине.
 * @param chosenAnswer Выбранный пользователем ответ, если есть.
 * @param chooseAnswer Лямбда-функция, вызываемая при выборе ответа пользователем. Если null, выбор ответов отключен.
 * @param confirmAnswer Лямбда-функция для подтверждения выбранного ответа. Если null, кнопка подтверждения не отображается.
 * @param isItResult Флаг, указывающий, отображается ли компонент в режиме показа результата (правильного/неправильного ответа).
 * Если null, режим неактивен.
 */
@Composable
internal fun Question(
    currentQuestion: QuizQuestion,
    numberOfCurrentQuestion: Int,
    quantityOfQuestions: Int,
    chosenAnswer: String?,
    chooseAnswer: ((String) -> Unit)? = null,
    confirmAnswer: (() -> Unit)? = null,
    isItResult: Boolean? = null
) {
    Card(
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius)),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.cardBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if (isItResult != null) Arrangement.SpaceBetween else Arrangement.Center
            ) {
                Text(
                    text = "Вопрос $numberOfCurrentQuestion из $quantityOfQuestions",
                    color = AppTheme.colors.questionNumberColor
                )
                if (isItResult != null) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            if (isItResult) R.drawable.correct_circle
                            else R.drawable.incorrect_circle
                        ),
                        contentDescription = stringResource(R.string.answer_state)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = currentQuestion.question,
                textAlign = TextAlign.Center,
                color = AppTheme.colors.text
            )
            Spacer(modifier = Modifier.height(24.dp))
            currentQuestion.allAnswers.forEach { answer ->
                Answer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = answer,
                    answerState = when {
                        // Выбран пользователем, но не подтвержден
                        answer == chosenAnswer && currentQuestion.chosenAnswer == null ->
                            AnswerState.Chosen

                        // Подтвержден правильный ответ
                        currentQuestion.chosenAnswer != null &&
                                answer == currentQuestion.chosenAnswer &&
                                answer == currentQuestion.correctAnswer ->
                            AnswerState.Correct

                        // Подтвержден неправильный ответ
                        currentQuestion.chosenAnswer != null &&
                                answer == currentQuestion.chosenAnswer &&
                                answer != currentQuestion.correctAnswer ->
                            AnswerState.Incorrect

                        // Все остальные ответы свободны (без выбора)
                        else -> AnswerState.Free
                    },
                    onClick = if (currentQuestion.chosenAnswer == null && chooseAnswer != null) {
                        { chooseAnswer(answer) }
                    } else null
                )
            }
            if (confirmAnswer != null) {
                RounderCornerButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = confirmAnswer,
                    enabled = chosenAnswer != null,
                    containerColor = AppTheme.colors.background
                ) {
                    Text(
                        text = if (quantityOfQuestions == numberOfCurrentQuestion)
                            stringResource(R.string.complete)
                        else stringResource(R.string.onward),
                        color = AppTheme.colors.textOnBackground
                    )
                }
            }
        }
    }
}
