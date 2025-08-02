package ru.vafeen.data.network.converter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.vafeen.data.network.dto.QuizQuestionDto
import ru.vafeen.domain.models.QuizQuestion

/**
 * Тесты для функции расширения [QuizQuestionDto.toDomainModel],
 * преобразующей DTO вопроса в доменную модель с перемешанными вариантами ответов.
 */
internal class QuizQuestionConverterTest {

    /**
     * Проверяет, что все поля корректно скопированы из DTO в доменную модель.
     */
    @Test
    fun `преобразование сохраняет все основные поля`() {
        val dto = QuizQuestionDto(
            type = "multiple",
            difficulty = "easy",
            category = "Animals",
            question = "What is the fastest land animal?",
            correctAnswer = "Cheetah",
            incorrectAnswers = listOf("Lion", "Gazelle", "Antelope")
        )

        val domainModel = dto.toDomainModel()
        assertEquals(dto.question, domainModel.question)
        assertEquals(dto.category, domainModel.category)
        assertEquals(dto.difficulty, domainModel.difficulty)
        assertEquals(dto.correctAnswer, domainModel.correctAnswer)
    }

    /**
     * Проверяет, что в [QuizQuestion.allAnswers] содержится правильный ответ и все неправильные ответы.
     */
    @Test
    fun `allAnswers содержит все ответы из DTO`() {
        val incorrects = listOf("Lion", "Gazelle", "Antelope")
        val correct = "Cheetah"
        val dto = QuizQuestionDto(
            type = "multiple",
            difficulty = "easy",
            category = "Animals",
            question = "What is the fastest land animal?",
            correctAnswer = correct,
            incorrectAnswers = incorrects
        )

        val domainModel = dto.toDomainModel()

        // Проверяем по множествам, чтобы порядок не имел значения
        val expectedAnswers = incorrects + correct
        assertEquals(expectedAnswers.toSet(), domainModel.allAnswers.toSet())
    }

    /**
     * Проверяет, что вызов перемешивает порядок ответов (т.е. результат не всегда одинаков).
     */
    @Test
    fun `allAnswers перемешивает порядок ответов`() {
        val incorrects = listOf("Lion", "Gazelle", "Antelope")
        val correct = "Cheetah"
        val dto = QuizQuestionDto(
            type = "multiple",
            difficulty = "easy",
            category = "Animals",
            question = "What is the fastest land animal?",
            correctAnswer = correct,
            incorrectAnswers = incorrects
        )

        // Создаем несколько раз, чтобы проверить что порядок меняется
        val first = dto.toDomainModel().allAnswers
        val second = dto.toDomainModel().allAnswers

        // Порядок должен отличаться хотя бы раз из нескольких (не гарантируется, но очень вероятно)
        if (first == second) {
            // Повторяем еще раз, если повезло совпасть
            val third = dto.toDomainModel().allAnswers
            assertNotEquals("Ожидается, что порядок будет перемешан", first, third)
        } else {
            assertTrue(true)
        }
    }

    /**
     * Проверяет поведение при отсутствии неправильных ответов (список пустой).
     */
    @Test
    fun `работа с пустым списком неправильных ответов`() {
        val correct = "Cheetah"
        val dto = QuizQuestionDto(
            type = "boolean",
            difficulty = "medium",
            category = "Animals",
            question = "Is Cheetah the fastest land animal?",
            correctAnswer = correct,
            incorrectAnswers = emptyList()
        )

        val domainModel = dto.toDomainModel()

        assertEquals(1, domainModel.allAnswers.size)
        assert(correct in domainModel.allAnswers)
    }
}
