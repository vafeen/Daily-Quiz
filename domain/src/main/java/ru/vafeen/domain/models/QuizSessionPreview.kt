package ru.vafeen.domain.models

import java.time.LocalDateTime

/**
 * Доменная модель для отображения превью (краткой информации) сессии викторины
 * без подробного списка результатов вопросов.
 *
 * Используется в списках истории или других кратких представлениях.
 *
 * @property dateTime Дата и время прохождения викторины.
 * @property name Название или имя сессии/пользователя.
 * @property countOfRightAnswers Количество правильно отвеченных вопросов.
 */
data class QuizSessionPreview(
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val name: String,
    val countOfRightAnswers: Int,
)
