package ru.vafeen.domain.network.repository

import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.network.ResponseResult

/**
 * Репозиторий для получения вопросов викторины из удалённого источника (например, сети).
 */
interface QuizNetworkRepository {

    /**
     * Выполняет асинхронный запрос на получение списка вопросов викторины.
     *
     * @return [ResponseResult] с успешным результатом — списком [QuizQuestion] либо с ошибкой.
     */
    suspend fun getQuiz(): ResponseResult<List<QuizQuestion>>
}
