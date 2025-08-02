package ru.vafeen.data.network.repository

import ru.vafeen.data.network.converter.toDomainModel
import ru.vafeen.data.network.service.QuizService
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.QuizNetworkRepository
import javax.inject.Inject

/**
 * Репозиторий для получения вопросов викторины из сети через Retrofit.
 *
 * @property quizService Сервис для выполнения сетевых запросов вопросов викторины.
 */
internal class RetrofitQuizNetworkRepository @Inject constructor(
    private val quizService: QuizService
) : QuizNetworkRepository {

    /**
     * Выполняет сетевой запрос на получение списка вопросов викторины.
     *
     * @return [ResponseResult] — успешный результат с данными [List]<[QuizQuestion]> или ошибка с описанием.
     */
    override suspend fun getQuiz(): ResponseResult<List<QuizQuestion>> = try {
        val response = quizService.getQuiz()
        if (response.responseCode == 0) {
            val questions = response.results.map { it.toDomainModel() }
            ResponseResult.Success(questions)
        } else {
            ResponseResult.Error("API вернул ошибочный код: ${response.responseCode}")
        }
    } catch (e: Exception) {
        ResponseResult.Error(e.stackTraceToString())
    }
}
