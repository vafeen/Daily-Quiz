package ru.vafeen.data.local_database.converter


import ru.vafeen.data.local_database.entity.QuizQuestionResultEntity
import ru.vafeen.data.local_database.entity.QuizSessionEntity
import ru.vafeen.data.local_database.tuple.QuizSessionWithResultsTuple
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.models.QuizSessionPreview
import ru.vafeen.domain.models.QuizSessionResult
import java.time.LocalDateTime

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
 * Расширение для конвертации сущности Room [QuizSessionEntity] в доменную модель
 * превью сессии викторины с форматированными датой и временем.
 *
 * @param months Список названий месяцев (например, на русском), индексируется с 0 (январь).
 * @return Доменная модель превью сессии [QuizSessionPreview] с отформатированными полями date и time.
 */
internal fun QuizSessionEntity.toDomainPreview(months: List<String>): QuizSessionPreview =
    QuizSessionPreview(
        sessionId = sessionId,
        date = formatDate(dateTime, months),
        time = formatTime(dateTime),
        name = name,
        countOfRightAnswers = countOfRightAnswers
    )

/**
 * Форматирует дату из [LocalDateTime] в строку вида "9 июля" с использованием списка месяцев.
 *
 * @param dateTime Дата и время, из которых берется день и месяц.
 * @param months Список названий месяцев, начиная с января (индекс 0).
 * @return Строка с форматом "день месяц", например "9 июля".
 */
private fun formatDate(dateTime: LocalDateTime, months: List<String>): String {
    val day = dateTime.dayOfMonth
    val monthIndex = dateTime.monthValue - 1
    val monthName = months.getOrNull(monthIndex) ?: "?"
    return "$day $monthName"
}

/**
 * Форматирует время из [LocalDateTime] в строку вида "9:30" без ведущих нулей у часов и с ведущими нулями у минут.
 *
 * @param dateTime Дата и время, из которых берутся часы и минуты.
 * @return Строка с форматом "час:минуты", например "9:05" или "15:30".
 */
private fun formatTime(dateTime: LocalDateTime): String {
    val hour = dateTime.hour
    val minute = dateTime.minute
    return if (minute < 10) "$hour:0$minute" else "$hour:$minute"
}