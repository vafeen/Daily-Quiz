package ru.vafeen.presentation.navigation

/**
 * Навигационные интенты корневого навигационного компонента.
 * Используются для управления навигацией: возврат назад, добавление экрана в стек или старт викторины.
 */
internal sealed class NavRootIntent {

    /**
     * Интент для навигации назад (эквивалент нажатия кнопки "назад").
     */
    data object Back : NavRootIntent()

    /**
     * Интент для добавления нового экрана в стек навигации.
     *
     * @property screen экран, который необходимо добавить в стек навигации
     */
    data class AddToBackStack(val screen: Screen) : NavRootIntent()

    /**
     * Интент для запуска викторины.
     * Используется для инициирования начала нового сеанса викторины.
     */
    data object StartTheQuiz : NavRootIntent()
}
