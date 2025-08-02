package ru.vafeen.data.local_database.tuple

import java.time.LocalDateTime

/**
 * Модель превью сессии викторины без списка результатов вопросов.
 *
 * Используется для быстрой выборки краткой информации из базы без загрузки вложенных данных.
 *
 * @property sessionId Идентификатор сессии (миллисекунды).
 * @property dateTime Дата и время прохождения викторины.
 * @property name Название или имя сессии/пользователя.
 * @property countOfRightAnswers Количество правильных ответов в сессии.
 */
internal data class QuizSessionPreviewTuple(
    val sessionId: Long,
    val dateTime: LocalDateTime,
    val name: String,
    val countOfRightAnswers: Int
)