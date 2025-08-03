package ru.vafeen.presentation.ui.screens.history_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.navigation.SendRootIntent
import ru.vafeen.presentation.ui.components.QuizHistoryInfoComponent
import ru.vafeen.presentation.ui.components.YouNeverTakenAnyQuizzes

/**
 * Экран истории сессий викторины.
 *
 * Отображает список превью сессий викторины, если они есть,
 * либо сообщение и кнопку для начала новой викторины в случае отсутствия историй.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигационный обработчик.
 */
@Composable
internal fun HistoryScreen(
    sendRootIntent: SendRootIntent,
) {
    val viewModel: HistoryViewModel =
        hiltViewModel<HistoryViewModel, HistoryViewModel.Factory>(creationCallback = { factory ->
            factory.create(sendRootIntent)
        })
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 27.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {

                Text(
                    modifier = Modifier.padding(top = 32.dp, bottom = 40.dp),
                    text = stringResource(R.string.history),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(24.dp),
                    onClick = { viewModel.handleIntent(HistoryIntent.Back) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }

            if (state.sessions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(
                        items = state.sessions,
                        key = { it.sessionId }
                    ) { session ->
                        session.QuizHistoryInfoComponent(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.handleIntent(
                                    HistoryIntent.NavigateToSession(session.sessionId)
                                )
                            }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 76.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    YouNeverTakenAnyQuizzes {
                        viewModel.handleIntent(HistoryIntent.ReturnToBeginning)
                    }

                    Image(
                        modifier = Modifier
                            .size(width = 180.dp, height = 40.dp)
                            .align(Alignment.CenterHorizontally),
                        painter = painterResource(R.drawable.daily_quiz),
                        contentDescription = stringResource(R.string.daily_quiz)
                    )
                }
            }
        }
    }
}
