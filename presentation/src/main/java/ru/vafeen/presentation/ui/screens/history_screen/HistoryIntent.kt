package ru.vafeen.presentation.ui.screens.history_screen

/**
 * Интенты (намерения пользователя) для экрана истории сессий викторины.
 */
internal sealed class HistoryIntent {

    /**
     * Навигация к подробной информации по сессии викторины.
     *
     * @property id Идентификатор выбранной сессии.
     */
    data class NavigateToSession(val id: Long) : HistoryIntent()

    /**
     * Начать новую сессию викторины.
     */
    object StartQuiz : HistoryIntent()
}
