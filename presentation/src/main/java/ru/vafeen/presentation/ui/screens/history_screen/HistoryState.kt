package ru.vafeen.presentation.ui.screens.history_screen

import ru.vafeen.domain.models.QuizSessionResultPreview

/**
 * Состояние экрана истории сессий викторины.
 *
 * @property sessions Список превью сессий викторины, отображаемых на экране.
 */
data class HistoryState(
    val sessions: List<QuizSessionResultPreview> = listOf()
)
