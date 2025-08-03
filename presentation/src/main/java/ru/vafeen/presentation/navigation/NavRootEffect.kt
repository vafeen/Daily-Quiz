package ru.vafeen.presentation.navigation

/**
 * Навигационные эффекты корневого навигационного компонента.
 * Используются для уведомления об изменениях в навигационном стеке,
 * таких как добавление экрана или возврат назад.
 */
internal sealed class NavRootEffect {

    /**
     * Эффект добавления экрана в стек навигации.
     *
     * @property screen экран, который нужно добавить в стек
     */
    data class AddToBackStack(val screen: Screen) : NavRootEffect()

    /**
     * Эффект навигации назад (эквивалент действия "назад").
     */
    data object Back : NavRootEffect()
}
