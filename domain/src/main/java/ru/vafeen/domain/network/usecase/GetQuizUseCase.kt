package ru.vafeen.domain.network.usecase

import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.network.ResponseResult
import ru.vafeen.domain.network.repository.QuizNetworkRepository

/**
 * Юзкейс для получения списка вопросов викторины.
 *
 * Инкапсулирует логику вызова сетевого репозитория для получения данных викторины.
 *
 * @property quizNetworkRepository Репозиторий для загрузки вопросов викторины из сети.
 */
class GetQuizUseCase(
    private val quizNetworkRepository: QuizNetworkRepository
) {

    /**
     * Выполняет получение списка вопросов викторины.
     *
     * @return [ResponseResult] с успешным списком [QuizQuestion] или ошибкой.
     */
    suspend operator fun invoke(): ResponseResult<List<QuizQuestion>> =
        quizNetworkRepository.getQuiz()
}
