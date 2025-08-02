package ru.vafeen.data.network.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO для парсинга ответа API викторины.
 *
 * @property responseCode Код ответа API (0 — успешный запрос).
 * @property results Список вопросов викторины в формате DTO.
 */
internal data class QuizResponse(
    @SerializedName("response_code") val responseCode: Int,
    val results: List<QuizQuestionDto>
)