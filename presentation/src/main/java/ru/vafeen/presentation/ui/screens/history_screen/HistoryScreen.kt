package ru.vafeen.presentation.ui.screens.history_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.navigation.SendRootIntent
import ru.vafeen.presentation.ui.components.QuizHistoryInfoComponent
import ru.vafeen.presentation.ui.components.YouNeverTakenAnyQuizzes
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.utils.pxToDp

/**
 * Экран истории сессий викторины.
 *
 * Позволяет просматривать историю сессий, запускать навигацию к сессии,
 * а также долгим удержанием вызвать меню для удаления сессии.
 *
 * Затемняет остальные элементы при открытом меню.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов.
 */
@Composable
internal fun HistoryScreen(
    sendRootIntent: SendRootIntent,
) {
    val context = LocalContext.current
    val viewModel: HistoryViewModel =
        hiltViewModel<HistoryViewModel, HistoryViewModel.Factory>(creationCallback = { factory ->
            factory.create(sendRootIntent)
        })
    val state by viewModel.state.collectAsState()

    var selectedSessionForMenu by remember { mutableStateOf<Long?>(null) }
    val dropdownExpanded = selectedSessionForMenu != null
    var intSize by remember { mutableStateOf(IntSize(0, 0)) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 27.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()// Затемняем все элементы, кроме выбранного, при открытом меню
                .graphicsLayer {
                    alpha = if (dropdownExpanded) 0.5f else 1f
                }) {

            Text(
                modifier = Modifier.padding(top = 32.dp, bottom = 20.dp),
                text = stringResource(R.string.history),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.textOnBackground
            )

            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp),
                onClick = { viewModel.handleIntent(HistoryIntent.Back) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = AppTheme.colors.textOnBackground
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
                    val isSelected = selectedSessionForMenu == session.sessionId

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Затемняем все элементы, кроме выбранного, при открытом меню
                            .graphicsLayer {
                                alpha = if (dropdownExpanded && !isSelected) 0.5f else 1f
                            },
                    ) {
                        session.QuizHistoryInfoComponent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged {
                                    intSize = it
                                },
                            innerModifier = Modifier
                                // Долгое нажатие — выделить элемент и открыть меню
                                .combinedClickable(
                                    onClick = {
                                        viewModel.handleIntent(
                                            HistoryIntent.NavigateToSession(session.sessionId)
                                        )
                                    },
                                    onLongClick = {
                                        selectedSessionForMenu = session.sessionId
                                    }
                                ),
                        )

                        if (isSelected) {
                            DropdownMenu(
                                shape = RoundedCornerShape(25.dp),
                                expanded = dropdownExpanded,
                                onDismissRequest = { selectedSessionForMenu = null },
                                modifier = Modifier
                                    .background(Color.White)
                                    .width((intSize.width.pxToDp(context) * 2 / 3).dp),
                                offset = DpOffset((intSize.width.pxToDp(context) / 6).dp, 20.dp)
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(R.string.delete),
                                            color = Color.Red,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    },
                                    trailingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_delete),
                                            contentDescription = stringResource(R.string.delete),
                                            tint = Color.Red
                                        )
                                    },
                                    onClick = {
                                        viewModel.handleIntent(
                                            HistoryIntent.DeleteSession(
                                                session.sessionId
                                            )
                                        )
                                        selectedSessionForMenu = null
                                    },
                                )
                            }
                        }

                    }
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

                androidx.compose.foundation.Image(
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
