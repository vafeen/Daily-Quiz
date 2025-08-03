package ru.vafeen.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Тип для функции отправки навигационных интентов в корневой обработчик.
 *
 * @param NavRootIntent навигационный интент, который нужно обработать
 */
internal typealias SendRootIntent = (NavRootIntent) -> Unit

/**
 * ViewModel корневой навигации, отвечающий за обработку навигационных интентов
 * и генерацию эффектов для изменения состояния навигационного стека.
 *
 * @property _effects MutableSharedFlow с навигационными эффектами для подписчиков
 * @property effects SharedFlow с неизменяемым доступом к эффектам навигации
 */
@HiltViewModel
internal class NavRootViewModel @Inject constructor() : ViewModel() {
    private val _effects = MutableSharedFlow<NavRootEffect>()

    /**
     * Поток навигационных эффектов, доступный для подписчиков.
     */
    val effects = _effects.asSharedFlow()

    /**
     * Обрабатывает полученный навигационный интент, выполняя соответствующие действия.
     *
     * @param intent навигационный интент для обработки
     */
    fun handleIntent(intent: NavRootIntent) {
        viewModelScope.launch(Dispatchers.IO) {
            when (intent) {
                is NavRootIntent.AddToBackStack -> addToBackStack(intent.screen)
                NavRootIntent.Back -> back()
            }
        }
    }

    /**
     * Добавляет экран в стек навигации, испуская соответствующий эффект.
     *
     * @param screen экран для добавления в стек
     */
    private suspend fun addToBackStack(screen: Screen) {
        _effects.emit(NavRootEffect.AddToBackStack(screen))
    }

    /**
     * Выполняет навигацию назад, испуская эффект возврата.
     */
    private suspend fun back() {
        _effects.emit(NavRootEffect.Back)
    }
}
