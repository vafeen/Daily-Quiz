package ru.vafeen.presentation.ui.screens.history_screen

import ru.vafeen.domain.models.QuizSessionResultPreview

/**
 * Состояние экрана истории сессий викторины.
 *
 * @property sessions Список превью сессий викторины, отображаемых на экране.
 * @property widthDp Текущая ширина контейнера-карточки в dp для адаптивного отображения.
 * @property selectedSessionId Идентификатор выбранной сессии (например, для показа меню удаления),
 * либо null, если сессия не выбрана.
 */
data class HistoryState(
    val sessions: List<QuizSessionResultPreview> = listOf(),
    val widthDp: Float = 0f,
    val selectedSessionId: Long? = null,
)
