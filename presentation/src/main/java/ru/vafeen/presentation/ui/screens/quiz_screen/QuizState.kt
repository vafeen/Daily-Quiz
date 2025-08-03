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
     * @property chosenAnswer Выбранный ответ пользователем для текущего вопроса, или null если ответа нет.
     * @property currentQuestion Текущий вопрос для отображения в викторине.
     * @property questions Список оставшихся вопросов, которые нужно пройти.
     * @property passedQuestions Список уже пройденных вопросов с выбранными ответами.
     * @property currentSeconds Текущее количество прошедших секунд для текущего вопроса или таймера викторины.
     * @property quantityOfSeconds Общее количество секунд, отведённое на вопрос или викторину.
     * @property isDialogLoseShown Флаг, указывающий, отображено ли диалоговое окно проигрыша.
     */
    data class Quiz(
        val chosenAnswer: String? = null,
        val currentQuestion: QuizQuestion,
        val questions: List<QuizQuestion> = emptyList(),
        val passedQuestions: List<QuizQuestion> = emptyList(),
        val currentSeconds: Long = 0L,
        val quantityOfSeconds: Float = 300f,
        val isDialogLoseShown: Boolean = false,
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
