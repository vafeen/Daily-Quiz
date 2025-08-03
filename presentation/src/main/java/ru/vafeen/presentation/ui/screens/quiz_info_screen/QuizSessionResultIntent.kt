package ru.vafeen.presentation.ui.screens.quiz_info_screen

/**
 * Навигационные и пользовательские интенты для экрана результатов сессии викторины.
 *
 * Используются для обработки действий пользователя, таких как возврат назад или запуск новой викторины.
 */
internal sealed class QuizSessionResultIntent {

    /**
     * Интент для навигации назад (закрытие экрана результатов).
     */
    data object Back : QuizSessionResultIntent()

    /**
     * Интент для возврата к запуску новой викторины
     */
    data object ReturnToBeginning : QuizSessionResultIntent()
}
