package ru.vafeen.presentation.ui.screens.quiz_info_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.ui.components.Question
import ru.vafeen.presentation.ui.components.ResultComponent
import ru.vafeen.presentation.ui.components.RounderCornerButton

/**
 * Экран с результатами сессии викторины.
 *
 * Отображает итоговый результат викторины, подробный список вопросов с выбранными ответами
 * и предоставляет кнопку для повторного запуска викторины.
 *
 * @param sessionId уникальный идентификатор сессии викторины
 * @param sendRootIntent функция для отправки навигационных интентов в корневой навигационный обработчик
 */
@Composable
internal fun QuizSessionResultScreen(
    sessionId: Long,
    sendRootIntent: (NavRootIntent) -> Unit,
) {
    val viewModel: QuizSessionResultViewModel =
        hiltViewModel<QuizSessionResultViewModel, QuizSessionResultViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(
                    sessionId = sessionId,
                    sendRootIntent = sendRootIntent
                )
            })
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 27.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(24.dp),
                        onClick = { viewModel.handleIntent(QuizSessionResultIntent.Back) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = stringResource(R.string.back)
                        )
                    }

                    Text(
                        text = stringResource(R.string.results),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            state.quizResult?.let {
                item { ResultComponent(it) }
            }

            item {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Твои ответы",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            state.quizSessionResult?.results?.let { results ->
                results.forEachIndexed { index, question ->
                    item {
                        Question(
                            currentQuestion = question,
                            numberOfCurrentQuestion = index + 1,
                            quantityOfQuestions = results.size,
                            chosenAnswer = question.chosenAnswer,
                            chooseAnswer = null,
                            confirmAnswer = null,
                            isItResult = question.correctAnswer == question.chosenAnswer
                        )
                    }
                }
            }
            item {
                RounderCornerButton(
                    onClick = { viewModel.handleIntent(QuizSessionResultIntent.ReturnToBeginning) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    containerColor = Color.White,
                ) {
                    Text(
                        text = stringResource(R.string.start_over).uppercase(),
                        color = Color.Black
                    )
                }
            }
        }
    }
}
