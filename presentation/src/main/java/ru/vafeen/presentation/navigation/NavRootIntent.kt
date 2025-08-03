package ru.vafeen.presentation.navigation

/**
 * Навигационные интенты корневого навигационного компонента.
 * Используются для управления навигацией: возврат назад или добавление экрана в стек.
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

    data object StartTheQuiz : NavRootIntent()
}
