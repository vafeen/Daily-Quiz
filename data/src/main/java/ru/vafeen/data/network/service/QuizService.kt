package ru.vafeen.data.network.service

import retrofit2.http.GET
import ru.vafeen.data.network.dto.QuizResponse

/**
 * Сервис Retrofit для получения вопросов викторины с удалённого API.
 */
internal interface QuizService {

    /**
     * Запрашивает список вопросов викторины.
     *
     * @return [QuizResponse] — DTO с кодом ответа и списком вопросов викторины.
     */
    @GET("api.php?amount=5&difficulty=easy")
    suspend fun getQuiz(): QuizResponse

    companion object {
        const val BASE_URL = "https://opentdb.com/"
    }
}
