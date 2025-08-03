package ru.vafeen.domain.models

/**
 * Доменная модель для отображения превью (краткой информации) сессии викторины
 * без подробного списка результатов вопросов.
 *
 * Используется в списках истории или других кратких представлениях.
 *
 * @property sessionId Уникальный идентификатор сессии викторины (например, millis с эпохи).
 * @property date Строка с датой прохождения викторины, например, "9 июля".
 * @property time Строка с временем прохождения викторины, например, "9:30".
 * @property name Название или имя сессии/пользователя.
 * @property countOfRightAnswers Количество правильно отвеченных вопросов в сессии.
 */
data class QuizSessionResultPreview(
    val sessionId: Long,
    val date: String,
    val time: String,
    val name: String,
    val countOfRightAnswers: Int,
)
