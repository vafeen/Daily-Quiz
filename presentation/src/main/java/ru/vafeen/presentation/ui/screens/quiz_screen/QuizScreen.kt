package ru.vafeen.presentation.ui.screens.quiz_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.ui.components.Error
import ru.vafeen.presentation.ui.components.LoadingQuiz
import ru.vafeen.presentation.ui.components.Question
import ru.vafeen.presentation.ui.components.ResultComponent
import ru.vafeen.presentation.ui.components.Welcome

/**
 * Компонент экрана викторины, отображающий различные состояния викторины,
 * управляемые через [QuizViewModel].
 *
 * @param viewModel ViewModel для управления состояниями и обработкой интентов.
 */
@Composable
internal fun QuizScreen(
    isQuizStarted: Boolean,
    sendRootIntent: (NavRootIntent) -> Unit
) {
    val viewModel: QuizViewModel =
        hiltViewModel<QuizViewModel, QuizViewModel.Factory>(creationCallback = { factory ->
            factory.create(
                isQuizStarted = isQuizStarted,
                sendRootIntent = sendRootIntent
            )
        })
    val state by viewModel.state.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state !is QuizState.Quiz && state !is QuizState.Result) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (state is QuizState.Start || state is QuizState.Error) {
                        Button(
                            onClick = {
                                viewModel.handleIntent(QuizIntent.NavigateToHistory)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(
                                stringResource(R.string.history),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(R.drawable.history),
                                contentDescription = stringResource(R.string.history),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            if (state !is QuizState.Result) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (state is QuizState.Quiz) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(24.dp),
                            onClick = { viewModel.handleIntent(QuizIntent.ReturnToBeginning) }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arrow_back),
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                    Image(
                        modifier = Modifier.size(
                            width = if (state is QuizState.Quiz) 180.dp else 300.dp,
                            height = if (state is QuizState.Quiz) 40.dp else 68.dp
                        ),
                        painter = painterResource(R.drawable.daily_quiz),
                        contentDescription = stringResource(R.string.daily_quiz)
                    )
                }
            }

            when (state) {
                is QuizState.Start -> {
                    Welcome {
                        viewModel.handleIntent(QuizIntent.BeginQuiz)
                    }
                }

                is QuizState.Error -> {
                    Welcome {
                        viewModel.handleIntent(QuizIntent.BeginQuiz)
                    }
                    Error()
                }

                is QuizState.Loading -> {
                    LoadingQuiz()
                }

                is QuizState.Quiz -> {
                    Spacer(modifier = Modifier.height(40.dp))
                    Question(
                        state = state as QuizState.Quiz,
                        chooseAnswer = { answer ->
                            viewModel.handleIntent(QuizIntent.ChoseAnswer(answer))
                        },
                        confirmAnswer = {
                            viewModel.handleIntent(QuizIntent.ConfirmChosenAnswer)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.cant_return_to_previous_questions),
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }

                is QuizState.Result -> {
                    Spacer(modifier = Modifier.height(32.dp))
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .size(24.dp),
                            onClick = { viewModel.handleIntent(QuizIntent.ReturnToBeginning) }
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
                    Spacer(modifier = Modifier.height(40.dp))
                    ResultComponent(
                        state = state as QuizState.Result,
                        onTryAgainClick = {
                            viewModel.handleIntent(QuizIntent.BeginQuiz)
                        }
                    )
                }
            }
        }
    }
}
