package ru.vafeen.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import ru.vafeen.presentation.ui.screens.history_screen.HistoryScreen
import ru.vafeen.presentation.ui.screens.quiz_info_screen.QuizSessionResultScreen
import ru.vafeen.presentation.ui.screens.quiz_screen.QuizScreen
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Компонент навигации корневого уровня, отображающий текущий стек экранов и
 * обрабатывающий навигационные эффекты из [NavRootViewModel].
 *
 * Управляет переходами между экранами с анимациями появления/исчезновения.
 */
@Composable
internal fun NavRoot() {
    val viewModel = hiltViewModel<NavRootViewModel>()
    val backStack = rememberNavBackStack(Screen.QuizScreen)

    LaunchedEffect(null) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is NavRootEffect.AddToBackStack -> backStack.add(effect.screen)
                NavRootEffect.Back -> backStack.removeLastOrNull()
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppTheme.colors.background
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = {
                // Обработка нажатия "назад" - пересылаем интент в ViewModel
                viewModel.handleIntent(NavRootIntent.Back)
            },
            entryProvider = entryProvider {
                entry<Screen.QuizScreen> {
                    QuizScreen { intent ->
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
}
