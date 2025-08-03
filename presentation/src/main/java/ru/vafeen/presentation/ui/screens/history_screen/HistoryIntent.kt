package ru.vafeen.presentation.ui.screens.history_screen

/**
 * Интенты (намерения пользователя) для экрана истории сессий викторины.
 *
 * Используются для обработки действий пользователя на экране истории,
 * таких как навигация к подробной информации о сессии, возврат к началу и возврат назад,
 * а также для удаления сессии и управления состоянием выбора и ширины.
 */
internal sealed class HistoryIntent {

    /**
     * Навигация к подробной информации по сессии викторины.
     *
     * @property id Идентификатор выбранной сессии.
     */
    data class NavigateToSession(val id: Long) : HistoryIntent()

    /**
     * Интент возврата к начальному экрану викторины.
     * Отправляется при возврате пользователя к началу приложения или старту викторины.
     */
    data object ReturnToBeginning : HistoryIntent()

    /**
     * Интент навигации назад (эквивалент нажатия кнопки "назад").
     */
    data object Back : HistoryIntent()

    /**
     * Интент удаления сессии по идентификатору.
     *
     * @property sessionId Идентификатор сессии, которую необходимо удалить.
     */
    data class DeleteSession(val sessionId: Long) : HistoryIntent()

    /**
     * Интент обновления ширины в пикселях (например, ширины контейнера экрана).
     *
     * @property width Текущая ширина в пикселях.
     */
    data class UpdateWidth(val width: Int) : HistoryIntent()

    /**
     * Интент выбора или снятия выбора сессии (для управления контекстным меню).
     *
     * @property sessionId Идентификатор выбранной сессии, либо null если выбор снят.
     */
    data class SelectSession(val sessionId: Long?) : HistoryIntent()
}
