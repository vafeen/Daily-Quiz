package ru.vafeen.presentation.ui.screens.quiz_info_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.usecase.GetQuizSessionResultUseCase
import ru.vafeen.domain.models.QuizResult
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.navigation.SendRootIntent

/**
 * ViewModel экрана с результатами сессии викторины.
 *
 * Отвечает за загрузку результатов сессии, хранение состояния экрана и обработку навигационных интентов.
 *
 * @property sessionId уникальный идентификатор сессии викторины
 * @property sendRootIntent функция для отправки навигационных интентов в корневой навигационный обработчик
 * @property getQuizSessionResultUseCase use case для получения результатов сессии викторины
 */
@HiltViewModel(assistedFactory = QuizSessionResultViewModel.Factory::class)
internal class QuizSessionResultViewModel @AssistedInject constructor(
    @Assisted private val sessionId: Long,
    @Assisted private val sendRootIntent: SendRootIntent,
    private val getQuizSessionResultUseCase: GetQuizSessionResultUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(QuizSessionResultState())

    /**
     * Состояние экрана с результатами сессии викторины.
     */
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            loadResults()
        }
    }

    /**
     * Обрабатывает интенты от UI.
     *
     * @param intent интент пользователя с описанием действия
     */
    fun handleIntent(intent: QuizSessionResultIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                QuizSessionResultIntent.Back -> back()
                QuizSessionResultIntent.ReturnToBeginning -> {
                    back()
                    back()
                }
            }
        }
    }

    /**
     * Отправляет навигационный интент возврата назад.
     */
    private fun back() = sendRootIntent(NavRootIntent.Back)

    /**
     * Загружает результаты сессии викторины и обновляет состояние экрана.
     */
    private suspend fun loadResults() {
        val result = getQuizSessionResultUseCase.invoke(sessionId)
        _state.update {
            it.copy(
                quizResult = result?.let { QuizResult.getByCount(result.countOfRightAnswers) },
                quizSessionResult = result
            )
        }
    }

    /**
     * Фабрика для создания экземпляров [QuizSessionResultViewModel] с необходимыми параметрами.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр [QuizSessionResultViewModel].
         *
         * @param sessionId уникальный идентификатор сессии викторины
         * @param sendRootIntent функция для отправки навигационных интентов
         * @return новый экземпляр [QuizSessionResultViewModel]
         */
        fun create(
            sessionId: Long,
            sendRootIntent: SendRootIntent
        ): QuizSessionResultViewModel
    }
}
