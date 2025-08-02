package ru.vafeen.domain.models

/**
 * Доменная модель вопроса викторины для использования в приложении.
 *
 * Представляет собой вопрос с вариантами ответов и выбранным ответом (если есть).
 *
 * @property question Текст вопроса.
 * @property category Категория вопроса.
 * @property difficulty Сложность вопроса (например, "easy", "medium", "hard").
 * @property correctAnswer Правильный ответ на вопрос.
 * @property allAnswers Все варианты ответов (правильный и неправильные), перемешанные.
 * @property chosenAnswer Выбранный пользователем ответ, или null если ответ не выбран.
 */
data class QuizQuestion(
    val question: String,
    val category: String,
    val difficulty: String,
    val correctAnswer: String,
    val allAnswers: List<String>,
    val chosenAnswer: String? = null,
)