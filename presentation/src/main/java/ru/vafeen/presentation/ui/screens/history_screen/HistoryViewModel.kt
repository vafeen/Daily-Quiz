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
 * Отвечает за загрузку списка превью сессий, обновление состояния экрана
 * и обработку пользовательских интентов.
 *
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой обработчик.
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

    /**
     * Состояние экрана, содержащее список превью сессий.
     */
    val state = _state.asStateFlow()

    /**
     * Обрабатывает интенты, поступающие от UI, и выполняет соответствующие действия.
     *
     * @param intent интент пользователя, описывающий действие.
     */
    fun handleIntent(intent: HistoryIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is HistoryIntent.NavigateToSession -> navigateToSession(intent.id)
                HistoryIntent.ReturnToBeginning -> startQuiz()
                HistoryIntent.Back -> back()
            }
        }
    }

    init {
        val months = context.resources.getStringArray(R.array.months).toList()
        viewModelScope.launch(Dispatchers.IO) {
            getAllSessionPreviewUseCase.invoke(months)
                .map { it.reversed() }
                .collect { sessions ->
                    _state.update { it.copy(sessions = sessions) }
                }
        }
    }

    /**
     * Отправляет навигационный интент возврата назад.
     */
    private fun back() = sendRootIntent(NavRootIntent.Back)

    /**
     * Инициирует навигацию к экрану с результатами конкретной сессии викторины.
     *
     * @param sessionId уникальный идентификатор сессии викторины
     */
    private fun navigateToSession(sessionId: Long) =
        sendRootIntent(
            NavRootIntent.AddToBackStack(Screen.QuizSessionResult(sessionId = sessionId))
        )

    /**
     * Выполняет возврат на главный экран для запуска викторины.
     */
    private fun startQuiz() = sendRootIntent(NavRootIntent.Back)

    /**
     * Фабрика для создания экземпляров [HistoryViewModel] с необходимыми параметрами.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр [HistoryViewModel].
         *
         * @param sendRootIntent функция для отправки навигационных интентов
         * @return новый экземпляр [HistoryViewModel]
         */
        fun create(sendRootIntent: SendRootIntent): HistoryViewModel
    }
}
