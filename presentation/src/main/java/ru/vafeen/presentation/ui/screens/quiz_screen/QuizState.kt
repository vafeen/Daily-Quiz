package ru.vafeen.presentation.ui.screens.quiz_screen

import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.models.QuizResult

/**
 * Состояния экрана викторины.
 *
 * Представляет все возможные состояния UI экрана викторины.
 * Используется в сочетании с MVI-паттерном для управления отображением экрана.
 */
internal sealed class QuizState {
    /**
     * Начальное состояние экрана перед стартом викторины.
     * Отображает стартовый экран с кнопкой начала.
     */
    object Start : QuizState()

    /**
     * Состояние загрузки данных викторины.
     * Отображает индикатор загрузки.
     */
    object Loading : QuizState()

    /**
     * Состояние ошибки при загрузке викторины.
     * Отображает сообщение об ошибке и возможность повторить загрузку.
     */
    object Error : QuizState()

    /**
     * Состояние, при котором викторина активна с доступными вопросами.
     *
     * @property chosenAnswer Выбранный ответ пользователем для текущего вопроса (или null, если не выбран).
     * @property currentQuestion Текущий вопрос для отображения.
     * @property questions Список оставшихся вопросов для прохождения.
     * @property passedQuestions Список уже пройденных вопросов с выбранными ответами.
     */
    data class Quiz(
        val chosenAnswer: String? = null,
        val currentQuestion: QuizQuestion? = null,
        val questions: List<QuizQuestion> = emptyList(),
        val passedQuestions: List<QuizQuestion> = emptyList(),
    ) : QuizState()

    /**
     * Состояние результата викторины, отображаемое после прохождения всех вопросов.
     *
     * @property quizResult Итоговый результат викторины.
     */
    data class Result(
        val quizResult: QuizResult
    ) : QuizState()
}
