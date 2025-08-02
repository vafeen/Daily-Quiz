package ru.vafeen.data.local_database.converter


import ru.vafeen.data.local_database.entity.QuizQuestionResultEntity
import ru.vafeen.data.local_database.entity.QuizSessionEntity
import ru.vafeen.data.local_database.tuple.QuizSessionWithResultsTuple
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.models.QuizSessionPreview
import ru.vafeen.domain.models.QuizSessionResult

/**
 * Преобразование связанной сущности Room [QuizSessionWithResultsTuple]
 * в доменную модель [QuizSessionResult].
 *
 * @return Доменная модель сессии викторины с вложенными результатами вопросов.
 */
internal fun QuizSessionWithResultsTuple.toDomain(): QuizSessionResult = QuizSessionResult(
    dateTime = session.dateTime,
    name = session.name,
    countOfRightAnswers = session.countOfRightAnswers,
    results = results.map(QuizQuestionResultEntity::toDomain)
)

/**
 * Преобразует доменную модель [QuizQuestion] в сущность Room [QuizQuestionResultEntity].
 *
 * @param sessionId Идентификатор сессии викторины, к которой привязан вопрос.
 * @return Сущность [QuizQuestionResultEntity] для записи в базу данных.
 */
internal fun QuizQuestion.toEntity(
    sessionId: Long,
): QuizQuestionResultEntity = QuizQuestionResultEntity(
    sessionId = sessionId,
    question = question,
    category = category,
    difficulty = difficulty,
    correctAnswer = correctAnswer,
    allAnswers = allAnswers,
    chosenAnswer = chosenAnswer ?: "udnefined"
)


/**
 * Преобразование сущности Room [QuizQuestionResultEntity] в доменную модель [QuizQuestion].
 *
 * @receiver Сущность результата вопроса.
 * @return Доменная модель результата вопроса.
 */
internal fun QuizQuestionResultEntity.toDomain(): QuizQuestion = QuizQuestion(
    question = question,
    category = category,
    difficulty = difficulty,
    correctAnswer = correctAnswer,
    allAnswers = allAnswers,
    chosenAnswer = chosenAnswer
)

/**
 * Конвертация сущности сессии Room в доменную модель превью сессии.
 *
 * @return Доменная модель превью сессии викторины.
 */
internal fun QuizSessionEntity.toDomainPreview(): QuizSessionPreview =
    QuizSessionPreview(
        dateTime = dateTime,
        name = name,
        countOfRightAnswers = countOfRightAnswers
    )