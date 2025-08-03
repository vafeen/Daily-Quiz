package ru.vafeen.presentation.ui.screens.quiz_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.vafeen.domain.local_database.usecase.SaveQuizSessionResultUseCase
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.models.QuizResult
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.usecase.GetQuizUseCase
import ru.vafeen.presentation.navigation.NavRootIntent
import ru.vafeen.presentation.navigation.Screen
import ru.vafeen.presentation.navigation.SendRootIntent

/**
 * ViewModel экрана викторины, управляющая состояниями викторины и обработкой пользовательских действий.
 *
 * @property sendRootIntent функция для отправки навигационных интентов в корневой навигационный обработчик.
 * @property getQuizUseCase юзкейс для получения вопросов викторины.
 * @property saveQuizSessionResultUseCase юзкейс для сохранения результатов сессии викторины.
 */
@HiltViewModel(assistedFactory = QuizViewModel.Factory::class)
internal class QuizViewModel @AssistedInject constructor(
    @Assisted private val sendRootIntent: SendRootIntent,
    private val getQuizUseCase: GetQuizUseCase,
    private val saveQuizSessionResultUseCase: SaveQuizSessionResultUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<QuizState>(QuizState.Start)

    /**
     * Текущее состояние экрана викторины.
     */
    val state = _state.asStateFlow()

    private var timerJob: Job? = null

    /**
     * Обрабатывает интенты (действия) от UI.
     *
     * @param intent интент пользователя с описанием действия.
     */
    fun handleIntent(intent: QuizIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                QuizIntent.BeginQuiz -> beginQuiz()
                QuizIntent.NavigateToHistory -> navigateToHistory()
                QuizIntent.ReturnToBeginning -> returnToBeginning()
                is QuizIntent.ChoseAnswer -> choseAnswer(intent.answer)
                QuizIntent.ConfirmChosenAnswer -> confirmAnswer()
            }
        }
    }

    /**
     * Запускает таймер для викторины, который обновляет прошедшее время каждую секунду.
     * По истечении отведённого времени показывает диалог с проигрышем.
     */
    private fun startTimer() {
        timerJob = viewModelScope.launch(Dispatchers.IO) {
            val quantity = (_state.value as? QuizState.Quiz)?.quantityOfSeconds ?: return@launch
            val startMillis = System.currentTimeMillis()
            var currentMillis = startMillis
            while (isActive && (currentMillis - startMillis) / 1000 < quantity) {
                currentMillis = System.currentTimeMillis()
                _state.update { currentState ->
                    if (currentState is QuizState.Quiz) {
                        currentState.copy(currentSeconds = (currentMillis - startMillis) / 1000)
                    } else currentState
                }
            }
            _state.update { currentState ->
                if (currentState is QuizState.Quiz) {
                    currentState.copy(isDialogLoseShown = true)
                } else currentState
            }
        }
    }

    /**
     * Останавливает таймер, если он запущен.
     */
    private suspend fun stopTimer() {
        timerJob?.cancelAndJoin()
    }

    /**
     * Подтверждает выбранный ответ, обновляет состояние текущего вопроса либо переходит к следующему вопросу.
     * По окончании викторины вычисляет результат, устанавливает состояние результата и сохраняет сессию.
     */
    private suspend fun confirmAnswer() {
        val state = _state.value
        if (state is QuizState.Quiz) {
            val currentQuestion = state.currentQuestion
            // Если текущий вопрос ещё не подтверждён
            if (currentQuestion.chosenAnswer == null) {
                _state.update {
                    state.copy(currentQuestion = currentQuestion.copy(chosenAnswer = state.chosenAnswer))
                }
            } else {
                // Поднимаем состояние с оставшимися и пройденными вопросами
                val newStateWithNewQuestions = state.copy(
                    questions = state.questions.filter { it.question != state.currentQuestion.question },
                    passedQuestions = state.passedQuestions + state.currentQuestion
                )
                // Пробуем получить следующй вопрос
                val newCurrentQuestion = newStateWithNewQuestions.questions.firstOrNull()

                if (newCurrentQuestion != null) {
                    val newStateWithNewQuestionsAndAnswer = newStateWithNewQuestions.copy(
                        currentQuestion = newCurrentQuestion,
                        chosenAnswer = null
                    )
                    _state.update { newStateWithNewQuestionsAndAnswer }
                } else {
                    // Викторина завершена
                    stopTimer()
                    val countOfRightAnswers = newStateWithNewQuestions
                        .passedQuestions
                        .count { question -> question.chosenAnswer == question.correctAnswer }
                    _state.update {
                        QuizState.Result(
                            quizResult = QuizResult.getByCount(countOfRightAnswers)
                        )
                    }

                    saveQuizSessionResultUseCase.invoke(
                        countOfRightAnswers,
                        questions = newStateWithNewQuestions.passedQuestions
                    )
                }
            }
        }
    }

    /**
     * Устанавливает выбранный пользователем ответ в состояние.
     *
     * @param answer выбранный ответ.
     */
    private fun choseAnswer(answer: String) {
        val state = _state.value
        if (state is QuizState.Quiz) {
            _state.update { state.copy(chosenAnswer = answer) }
        }
    }

    /**
     * Возвращает состояние викторины к начальному экрану.
     */
    private suspend fun returnToBeginning() {
        stopTimer()
        _state.update { QuizState.Start }
    }

    /**
     * Отправляет навигационный интент для перехода к экрану истории.
     */
    private fun navigateToHistory() {
        sendRootIntent(NavRootIntent.AddToBackStack(Screen.HistoryScreen))
    }

    /**
     * Запускает загрузку вопросов викторины и обновляет состояние.
     * При успешной загрузке устанавливается состояние Quiz, иначе — Error.
     */
    private suspend fun beginQuiz() {
        _state.update { QuizState.Loading }
        when (val quizzes = getQuizUseCase.invoke()) {
            is ResponseResult.Success<List<QuizQuestion>> -> {
                _state.update {
                    QuizState.Quiz(
                        questions = quizzes.data,
                        currentQuestion = quizzes.data.first()
                    )
                }
                startTimer()
            }

            is ResponseResult.Error -> _state.update { QuizState.Error }
        }
    }

    /**
     * Фабрика для создания экземпляров [QuizViewModel] с необходимыми параметрами.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр [QuizViewModel].
         *
         * @param sendRootIntent функция для отправки навигационных интентов.
         * @return новый экземпляр [QuizViewModel].
         */
        fun create(sendRootIntent: SendRootIntent): QuizViewModel
    }
}
