package ru.vafeen.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ru.vafeen.presentation.ui.screens.history_screen.HistoryScreen
import ru.vafeen.presentation.ui.screens.quiz_info_screen.QuizSessionResultScreen
import ru.vafeen.presentation.ui.screens.quiz_screen.QuizScreen

/**
 * Компонент навигации корневого уровня, отображающий текущий стек экранов и
 * обрабатывающий навигационные эффекты из [NavRootViewModel].
 *
 * Управляет переходами между экранами с анимациями появления/исчезновения.
 */
@Composable
internal fun NavRoot() {
    val viewModel = hiltViewModel<NavRootViewModel>()
    val backStack = rememberNavBackStack(Screen.QuizScreen(isQuizStarted = false))

    LaunchedEffect(null) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is NavRootEffect.AddToBackStack -> backStack.add(effect.screen)
                NavRootEffect.Back -> backStack.removeLastOrNull()
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = {
            // Обработка нажатия "назад" - пересылаем интент в ViewModel
            viewModel.handleIntent(NavRootIntent.Back)
        },
        entryProvider = entryProvider {
            entry<Screen.QuizScreen> {
                QuizScreen(isQuizStarted = it.isQuizStarted) { intent ->
                    viewModel.handleIntent(intent)
                }
            }
            entry<Screen.HistoryScreen> {
                HistoryScreen { intent ->
                    viewModel.handleIntent(intent)
                }
            }
            entry<Screen.QuizSessionResult> {
                QuizSessionResultScreen(sessionId = it.sessionId) { intent ->
                    viewModel.handleIntent(intent)
                }
            }
//            entry<ScreenC>(
//                metadata = NavDisplay.transitionSpec {
//                    // Slide new content up, keeping the old content in place underneath
//                    slideInVertically(
//                        initialOffsetY = { it },
//                        animationSpec = tween(1000)
//                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
//                } + NavDisplay.popTransitionSpec {
//                    // Slide old content down, revealing the new content in place underneath
//                    EnterTransition.None togetherWith
//                            slideOutVertically(
//                                targetOffsetY = { it },
//                                animationSpec = tween(1000)
//                            )
//                } + NavDisplay.predictivePopTransitionSpec {
//                    // Slide old content down, revealing the new content in place underneath
//                    EnterTransition.None togetherWith
//                            slideOutVertically(
//                                targetOffsetY = { it },
//                                animationSpec = tween(1000)
//                            )
//                }
//            ) {
//                ContentGreen("This is Screen C")
//            }
        },
        transitionSpec = {
            // Анимация перехода вперед: сдвиг экрана слева направо
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Анимация возврата назад: сдвиг экрана справа налево
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Предиктивный переход назад - аналогично анимации возврата
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    )
}
