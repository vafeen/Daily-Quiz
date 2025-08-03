package ru.vafeen.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Представляет экраны приложения в виде sealed класса для навигации.
 * Каждый экран реализует интерфейс [NavKey] для интеграции с навигационной системой.
 */
internal sealed class Screen : NavKey {

    /**
     * Экран викторины.
     */
    @Serializable
    data object QuizScreen : Screen()

    /**
     * Экран истории.
     */
    @Serializable
    data object HistoryScreen : Screen()

    /**
     * Экран результата сессии викторины.
     *
     * @property sessionId уникальный идентификатор сессии викторины
     */
    @Serializable
    data class QuizSessionResult(val sessionId: Long) : Screen()
}
