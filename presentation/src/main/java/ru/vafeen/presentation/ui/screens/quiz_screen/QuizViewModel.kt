package ru.vafeen.presentation.ui.screens.quiz_screen

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
 * @property getQuizUseCase use case для загрузки вопросов викторины.
 */
@HiltViewModel(assistedFactory = QuizViewModel.Factory::class)
internal class QuizViewModel @AssistedInject constructor(
    @Assisted private val isQuizStarted: Boolean,
    @Assisted private val sendRootIntent: SendRootIntent,
    private val getQuizUseCase: GetQuizUseCase,
    private val saveQuizSessionResultUseCase: SaveQuizSessionResultUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<QuizState>(QuizState.Start)
    val state = _state.asStateFlow()

    /**
     * Обрабатывает интенты (действия) от UI.
     *
     * @param intent Интент пользователя.
     */
    fun handleIntent(intent: QuizIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                QuizIntent.BeginQuiz -> beginQuiz()
                QuizIntent.NavigateToHistory -> navigateToHistory()
                QuizIntent.ReturnToBeginning -> returnToBeginning()
                is QuizIntent.ChoseAnswer -> choseAnswer(intent.answer)
                QuizIntent.ConfirmChosenAnswer -> confirmAnswer()
                QuizIntent.TryAgain -> tryAgain()
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (isQuizStarted) {
                beginQuiz()
            }
        }
    }

    /**
     * Подтверждает выбранный ответ, обновляет состояние текущего вопроса либо переходит к следующему,
     * а по окончании викторины вычисляет результат и переходит в состояние результата.
     */
    private suspend fun confirmAnswer() {
        val state = _state.value
        if (state is QuizState.Quiz) {
            val currentQuestion = state.currentQuestion
            if (currentQuestion != null) {
                if (currentQuestion.chosenAnswer == null) {
                    _state.update {
                        state.copy(currentQuestion = currentQuestion.copy(chosenAnswer = state.chosenAnswer))
                    }
                } else {
                    val newStateWithNewQuestions = state.copy(
                        questions = state.questions.filter { it.question != state.currentQuestion.question },
                        passedQuestions = state.passedQuestions.plus(state.currentQuestion)
                    )
                    val newCurrentQuestion = newStateWithNewQuestions.questions.firstOrNull()
                    val newStateWithNewQuestionsAndAnswer = newStateWithNewQuestions.copy(
                        currentQuestion = newCurrentQuestion,
                        chosenAnswer = null
                    )
                    _state.update { newStateWithNewQuestionsAndAnswer }
                    if (newCurrentQuestion == null) {
                        val countOfRightAnswers = newStateWithNewQuestionsAndAnswer
                            .passedQuestions
                            .count { question -> question.chosenAnswer == question.correctAnswer }

                        _state.update {
                            QuizState.Result(
                                quizResult = QuizResult.getByCount(countOfRightAnswers)
                            )
                        }
                        saveQuizSessionResultUseCase.invoke(
                            countOfRightAnswers,
                            questions = newStateWithNewQuestionsAndAnswer.passedQuestions
                        )
                    }
                }
            }
        }
    }

    /**
     * Устанавливает выбранный ответ в состояние.
     *
     * @param answer Выбранный пользователем ответ.
     */
    private fun choseAnswer(answer: String) {
        val state = _state.value
        if (state is QuizState.Quiz) {
            _state.update { state.copy(chosenAnswer = answer) }
        }
    }

    /**
     * Возвращает состояние к началу викторины.
     */
    private fun returnToBeginning() {
        _state.update { QuizState.Start }
    }

    /**
     * Обрабатывает навигацию к экрану истории.
     * Пока не реализовано.
     */
    private fun navigateToHistory() {
        sendRootIntent(NavRootIntent.AddToBackStack(Screen.HistoryScreen))
    }

    /**
     *  Возвращает состояние викторины к начальному состоянию.
     */
    private fun tryAgain() {
        _state.update { QuizState.Start }
    }

    /**
     * Запускает загрузку вопросов викторины и обновляет состояние.
     */
    private suspend fun beginQuiz() {
        _state.update { QuizState.Loading }
        when (val quizzes = getQuizUseCase.invoke()) {
            is ResponseResult.Success<List<QuizQuestion>> -> _state.update {
                QuizState.Quiz(
                    questions = quizzes.data,
                    currentQuestion = quizzes.data.firstOrNull()
                )
            }

            is ResponseResult.Error -> _state.update { QuizState.Error }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(isQuizStarted: Boolean, sendRootIntent: SendRootIntent): QuizViewModel
    }
}
