package ru.vafeen.data.local_database

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.vafeen.data.local_database.converter.formatDate
import ru.vafeen.data.local_database.converter.formatTime
import ru.vafeen.data.local_database.converter.toDomain
import ru.vafeen.data.local_database.converter.toDomainPreview
import ru.vafeen.data.local_database.converter.toEntity
import ru.vafeen.data.local_database.entity.QuizQuestionResultEntity
import ru.vafeen.data.local_database.entity.QuizSessionEntity
import ru.vafeen.data.local_database.tuple.QuizSessionWithResultsTuple
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.models.QuizSessionResult
import java.time.LocalDateTime

class ConverterTests {

    /**
     * Проверяет корректное преобразование связанной сущности [QuizSessionWithResultsTuple]
     * в доменную модель [QuizSessionResult] с вложенными результатами вопросов.
     */
    @Test
    fun `toDomain converts QuizSessionWithResultsTuple to QuizSessionResult correctly`() {
        val sessionEntity = QuizSessionEntity(
            sessionId = 1L,
            dateTime = LocalDateTime.of(2023, 7, 15, 14, 5),
            name = "Test Session",
            countOfRightAnswers = 3
        )
        val questionEntity1 = QuizQuestionResultEntity(
            sessionId = 1L,
            question = "Q1",
            category = "Cat",
            difficulty = "Easy",
            correctAnswer = "A1",
            allAnswers = listOf("A1", "A2"),
            chosenAnswer = "A1"
        )
        val questionEntity2 = questionEntity1.copy(question = "Q2", chosenAnswer = "A2")
        val tuple = QuizSessionWithResultsTuple(
            session = sessionEntity,
            results = listOf(questionEntity1, questionEntity2)
        )

        val domainModel = tuple.toDomain()

        assertEquals(sessionEntity.dateTime, domainModel.dateTime)
        assertEquals(sessionEntity.name, domainModel.name)
        assertEquals(sessionEntity.countOfRightAnswers, domainModel.countOfRightAnswers)
        assertEquals(2, domainModel.results.size)

        val question1 = domainModel.results[0]
        assertEquals(questionEntity1.question, question1.question)
        assertEquals(questionEntity1.chosenAnswer, question1.chosenAnswer)

        val question2 = domainModel.results[1]
        assertEquals(questionEntity2.question, question2.question)
        assertEquals(questionEntity2.chosenAnswer, question2.chosenAnswer)
    }

    /**
     * Проверяет, что [QuizQuestion.toEntity] корректно конвертирует доменную модель
     * в сущность с сохранением всех полей и fallback для null chosenAnswer.
     */
    @Test
    fun `toEntity converts QuizQuestion to QuizQuestionResultEntity with chosenAnswer fallback`() {
        val domainQuestionWithChosen = QuizQuestion(
            question = "Q",
            category = "Cat",
            difficulty = "Medium",
            correctAnswer = "Correct",
            allAnswers = listOf("Correct", "Wrong"),
            chosenAnswer = "Correct"
        )

        val entityWithChosen = domainQuestionWithChosen.toEntity(sessionId = 10L)
        assertEquals(10L, entityWithChosen.sessionId)
        assertEquals("Q", entityWithChosen.question)
        assertEquals("Correct", entityWithChosen.chosenAnswer) // выбранный ответ сохранён

        val domainQuestionWithoutChosen = domainQuestionWithChosen.copy(chosenAnswer = null)
        val entityWithoutChosen = domainQuestionWithoutChosen.toEntity(sessionId = 11L)
        assertEquals(11L, entityWithoutChosen.sessionId)
        assertEquals("udnefined", entityWithoutChosen.chosenAnswer) // fallback при null
    }

    /**
     * Проверяет корректное преобразование сущности базы [QuizQuestionResultEntity] в доменную модель [QuizQuestion].
     */
    @Test
    fun `toDomain converts QuizQuestionResultEntity to QuizQuestion correctly`() {
        val entity = QuizQuestionResultEntity(
            sessionId = 5L,
            question = "Question?",
            category = "SomeCategory",
            difficulty = "Hard",
            correctAnswer = "Answer",
            allAnswers = listOf("Answer", "Other"),
            chosenAnswer = "Other"
        )

        val domain = entity.toDomain()

        assertEquals(entity.question, domain.question)
        assertEquals(entity.category, domain.category)
        assertEquals(entity.difficulty, domain.difficulty)
        assertEquals(entity.correctAnswer, domain.correctAnswer)
        assertEquals(entity.allAnswers, domain.allAnswers)
        assertEquals(entity.chosenAnswer, domain.chosenAnswer)
    }

    /**
     * Тестирует конвертацию сущности сессии [QuizSessionEntity] в доменный объект превью с
     * правильным форматированием даты и времени.
     */
    @Test
    fun `toDomainPreview converts QuizSessionEntity to QuizSessionResultPreview with correct formatting`() {
        val sessionEntity = QuizSessionEntity(
            sessionId = 100L,
            dateTime = LocalDateTime.of(2023, 1, 9, 9, 5),
            name = "Sample",
            countOfRightAnswers = 4
        )
        val months = listOf(
            "января", "февраля", "марта", "апреля", "мая", "июня",
            "июля", "августа", "сентября", "октября", "ноября", "декабря"
        )

        val preview = sessionEntity.toDomainPreview(months)

        assertEquals(100L, preview.sessionId)
        assertEquals("9 января", preview.date)
        assertEquals("9:05", preview.time)
        assertEquals("Sample", preview.name)
        assertEquals(4, preview.countOfRightAnswers)
    }

    /**
     * Проверяет fallback при некорректном индексе месяца (отсутствии в списке months).
     */
    @Test
    fun `formatDate returns fallback with ? when month index is out of bounds`() {
        val months = listOf("Jan", "Feb", "Mar")
        val dateTime = LocalDateTime.of(2023, 5, 20, 0, 0) // май, индекс 4 (отсчет с 0 до 2)

        val formattedDate = formatDate(dateTime, months)

        assertEquals("20 ?", formattedDate)
    }

    /**
     * Проверяет корректное форматирование времени в виде "часы:минуты" с ведущим нулём у минут.
     */
    @Test
    fun `formatTime formats time with leading zero in minutes`() {
        val dateTimeWithSingleDigitMinutes = LocalDateTime.of(2023, 1, 1, 9, 5)
        val formatted1 = formatTime(dateTimeWithSingleDigitMinutes)
        assertEquals("9:05", formatted1)

        val dateTimeWithDoubleDigitMinutes = LocalDateTime.of(2023, 1, 1, 15, 30)
        val formatted2 = formatTime(dateTimeWithDoubleDigitMinutes)
        assertEquals("15:30", formatted2)
    }

    /**
     * Проверяет корректное форматирование времени в полночь (0:00).
     */
    @Test
    fun `formatTime formats midnight correctly`() {
        val midnightTime = LocalDateTime.of(2023, 1, 1, 0, 0)
        val formatted = formatTime(midnightTime)
        assertEquals("0:00", formatted)
    }

}
