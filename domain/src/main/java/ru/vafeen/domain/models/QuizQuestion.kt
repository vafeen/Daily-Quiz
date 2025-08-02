package ru.vafeen.domain.models

/**
 * Доменная модель вопроса викторины для использования в приложении.
 *
 * @property question Текст вопроса.
 * @property category Категория вопроса.
 * @property difficulty Сложность вопроса.
 * @property correctAnswer Правильный ответ на вопрос.
 * @property allAnswers Все варианты ответов (правильный и неправильные), перемешанные.
 */
data class QuizQuestion(
    val question: String,
    val category: String,
    val difficulty: String,
    val correctAnswer: String,
    val allAnswers: List<String>
)