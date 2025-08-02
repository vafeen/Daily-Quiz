package ru.vafeen.data.network.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.vafeen.data.network.dto.QuizQuestionDto
import ru.vafeen.data.network.dto.QuizResponse
import ru.vafeen.data.network.service.QuizService
import ru.vafeen.domain.network.ResponseResult

/**
 * Юнит-тесты для [RetrofitQuizNetworkRepository].
 */
internal class RetrofitQuizNetworkRepositoryTest {

    // Создаём мок при объявлении
    private val quizService: QuizService = mockk()

    // Инициализируем репозиторий с мок-сервисом
    private val repository: RetrofitQuizNetworkRepository by lazy {
        RetrofitQuizNetworkRepository(quizService)
    }

    /**
     * Тест успешного получения вопросов викторины.
     * Проверяет, что при responseCode = 0 данные корректно преобразуются в Success с нужным списком.
     */
    @Test
    fun `getQuiz() returns Success with data when responseCode is zero`() = runBlocking {
        val dto = QuizQuestionDto(
            type = "multiple",
            difficulty = "easy",
            category = "Animals",
            question = "What is fastest land animal?",
            correctAnswer = "Cheetah",
            incorrectAnswers = listOf("Lion", "Gazelle")
        )
        val quizResponse = QuizResponse(
            responseCode = 0,
            results = listOf(dto)
        )

        coEvery { quizService.getQuiz() } returns quizResponse

        val result = repository.getQuiz()

        assertTrue(result is ResponseResult.Success)
        val data = (result as ResponseResult.Success).data
        assertEquals(1, data.size)
        assertEquals(dto.question, data[0].question)
        assertEquals(dto.correctAnswer, data[0].correctAnswer)
    }

    /**
     * Тест обработки ошибки API, когда responseCode не равен 0.
     * Проверяет, что возвращается ResponseResult.Error с соответствующим сообщением.
     */
    @Test
    fun `getQuiz() returns Error when responseCode is not zero`() = runBlocking {
        val quizResponse = QuizResponse(
            responseCode = 1,
            results = emptyList()
        )

        coEvery { quizService.getQuiz() } returns quizResponse

        val result = repository.getQuiz()

        assertTrue(result is ResponseResult.Error)
        val error = (result as ResponseResult.Error).stacktrace
        assertTrue(error.contains("ошибочный код"))
    }

    /**
     * Тест обработки исключения при выполнении запроса.
     * Проверяет, что при выбрасывании исключения возвращается ResponseResult.Error с трассировкой.
     */
    @Test
    fun `getQuiz() returns Error on exception thrown`() = runBlocking {
        val exceptionMessage = "Network Error"
        coEvery { quizService.getQuiz() } throws RuntimeException(exceptionMessage)

        val result = repository.getQuiz()

        assertTrue(result is ResponseResult.Error)
        val error = (result as ResponseResult.Error).stacktrace
        assertTrue(error.contains(exceptionMessage))
    }
}
