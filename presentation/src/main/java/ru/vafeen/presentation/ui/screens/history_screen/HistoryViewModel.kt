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
import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository
import ru.vafeen.domain.local_database.usecase.GetAllSessionPreviewUseCase
import ru.vafeen.presentation.R
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.navigation.SendRootIntent
import ru.vafeen.presentation.utils.pxToDp

/**
 * ViewModel экрана истории сессий викторины.
 *
 * Отвечает за загрузку списка превью сессий, обновление состояния экрана
 * и обработку пользовательских интентов, включая навигацию, удаление сессии,
 * выбор сессии и обновление ширины в dp.
 *
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой обработчик.
 * @property context Контекст приложения для доступа к ресурсам и получения параметров дисплея.
 * @property getAllSessionPreviewUseCase UseCase для получения всех превью сессий из локального хранилища.
 * @property quizResultLocalRepository Репозиторий для работы с результатами викторины в локальном хранилище.
 */
@HiltViewModel(assistedFactory = HistoryViewModel.Factory::class)
internal class HistoryViewModel @AssistedInject constructor(
    @Assisted private val sendRootIntent: SendRootIntent,
    @ApplicationContext private val context: Context,
    private val getAllSessionPreviewUseCase: GetAllSessionPreviewUseCase,
    private val quizResultLocalRepository: QuizResultLocalRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())

    /**
     * Состояние экрана, содержащее список превью сессий, выбранную сессию и ширину в dp.
     */
    val state = _state.asStateFlow()

    /**
     * Обрабатывает интенты, поступающие от UI, и выполняет соответствующие действия.
     *
     * Поддерживаются интенты навигации, удаления сессии, обновления ширины, выбора сессии
     * и возврата к началу.
     *
     * @param intent интент пользователя, описывающий действие.
     */
    fun handleIntent(intent: HistoryIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is HistoryIntent.NavigateToSession -> navigateToSession(sessionId = intent.id)
                HistoryIntent.ReturnToBeginning -> startQuiz()
                HistoryIntent.Back -> back()
                is HistoryIntent.DeleteSession -> deleteSession(sessionId = intent.sessionId)
                is HistoryIntent.UpdateWidth -> updateWidth(width = intent.width)
                is HistoryIntent.SelectSession -> selectSession(intent.sessionId)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val months = context.resources.getStringArray(R.array.months).toList()
            getAllSessionPreviewUseCase.invoke(months)
                .map { it.reversed() }
                .collect { sessions ->
                    _state.update { it.copy(sessions = sessions) }
                }
        }
    }

    /**
     * Обновляет выбранную сессию в состоянии.
     *
     * @param sessionId Идентификатор выбранной сессии, или null для снятия выбора.
     */
    private fun selectSession(sessionId: Long?) =
        _state.update { it.copy(selectedSessionId = sessionId) }

    /**
     * Обновляет ширину экрана в dp в состоянии по переданному значению в пикселях.
     *
     * @param width Значение ширины в пикселях.
     */
    private fun updateWidth(width: Int) = _state.update { it.copy(widthDp = width.pxToDp(context)) }

    /**
     * Выполняет навигацию назад.
     */
    private fun back() = sendRootIntent(NavRootIntent.Back)

    /**
     * Инициирует навигацию к экрану с результатами указанной сессии викторины.
     *
     * @param sessionId Уникальный идентификатор сессии викторины.
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
     * Удаляет сессию по идентификатору и обновляет список превью.
     *
     * @param sessionId Уникальный идентификатор сессии, которую необходимо удалить.
     */
    private suspend fun deleteSession(sessionId: Long) {
        quizResultLocalRepository.deleteSession(sessionId)
        selectSession(null)
    }

    /**
     * Фабрика для создания экземпляров [HistoryViewModel] с необходимыми параметрами.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр [HistoryViewModel].
         *
         * @param sendRootIntent Функция для отправки навигационных интентов.
         * @return Новый экземпляр [HistoryViewModel].
         */
        fun create(sendRootIntent: SendRootIntent): HistoryViewModel
    }
}
