package ru.vafeen.presentation.utils

import ru.vafeen.presentation.utils.AnswerState.Chosen
import ru.vafeen.presentation.utils.AnswerState.Correct
import ru.vafeen.presentation.utils.AnswerState.Free
import ru.vafeen.presentation.utils.AnswerState.Incorrect


/**
 * Перечисление состояний ответа в викторине.
 *
 * @property Free Ответ ещё не выбран пользователем.
 * @property Chosen Ответ выбран пользователем, но ещё не проверен.
 * @property Correct Ответ выбран и подтверждён как правильный.
 * @property Incorrect Ответ выбран и подтверждён как неправильный.
 */
internal enum class AnswerState {
    Free,
    Chosen,
    Correct,
    Incorrect
}