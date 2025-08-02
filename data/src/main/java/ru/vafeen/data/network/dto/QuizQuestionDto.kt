package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO вопроса викторины, получаемого с API.
 *
 * @property type Тип вопроса (например, "multiple", "boolean").
 * @property difficulty Сложность вопроса ("easy", "medium", "hard").
 * @property category Категория вопроса.
 * @property question Текст вопроса.
 * @property correctAnswer Правильный ответ.
 * @property incorrectAnswers Список неправильных ответов.
 */
internal data class QuizQuestionDto(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>
)


