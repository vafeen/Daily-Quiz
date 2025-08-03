package ru.vafeen.presentation.ui.screens.history_screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.usecase.GetAllSessionPreviewUseCase
import ru.vafeen.presentation.R
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.navigation.SendRootIntent

/**
 * ViewModel экрана истории сессий викторины.
 *
 * Отвечает за загрузку списка превью сессий, обновление состояния экрана и обработку пользовательских интентов.
 *
 * @property context Контекст приложения для доступа к ресурсам.
 * @property getAllSessionPreviewUseCase UseCase для получения всех превью сессий.
 */
@HiltViewModel(assistedFactory = HistoryViewModel.Factory::class)
internal class HistoryViewModel @AssistedInject constructor(
    @Assisted private val sendRootIntent: SendRootIntent,
    @ApplicationContext private val context: Context,
    private val getAllSessionPreviewUseCase: GetAllSessionPreviewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    /**
     * Обработать интенты, пришедшие с UI.
     *
     * @param intent Интент пользователя, описывающий действие.
     */
    fun handleIntent(intent: HistoryIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is HistoryIntent.NavigateToSession -> navigateToSession(intent.id)
                HistoryIntent.StartQuiz -> startQuiz()
            }
        }
    }

    init {
        val months = context.resources.getStringArray(R.array.months).toList()
        viewModelScope.launch(Dispatchers.IO) {
            getAllSessionPreviewUseCase.invoke(months)
                .map { it.reversed() } // отображаем в обратном порядке (сначала новые)
                .collect { sessions ->
                    _state.update { it.copy(sessions = sessions) }
                }
        }
    }

    private fun navigateToSession(sessionId: Long) =
        sendRootIntent(
            NavRootIntent.AddToBackStack(Screen.QuizSessionResult(sessionId = sessionId))
        )


    private fun startQuiz() {
        // TODO(логика начала квиза)
    }

    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: SendRootIntent): HistoryViewModel
    }
}
