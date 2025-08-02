package ru.vafeen.domain.models

import java.time.LocalDateTime

/**
 * Доменная модель результата сессии викторины.
 *
 * Представляет одну сессию прохождения викторины с датой, именем,
 * количеством правильных ответов и списком результатов по вопросам.
 *
 * @property dateTime Дата и время прохождения сессии викторины.
 * @property name Имя или название сессии (например, имя пользователя или произвольное имя).
 * @property countOfRightAnswers Количество вопросов, на которые пользователь ответил правильно.
 * @property results Список результатов по каждому вопросу в сессии.
 */
data class QuizSessionResult(
    val dateTime: LocalDateTime,
    val name: String,
    val countOfRightAnswers: Int,
    val results: List<QuizQuestion>
)
