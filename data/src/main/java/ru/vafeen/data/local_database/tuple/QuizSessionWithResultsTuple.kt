package ru.vafeen.data.local_database.tuple

import androidx.room.Embedded
import androidx.room.Relation
import ru.vafeen.data.local_database.entity.QuizQuestionResultEntity
import ru.vafeen.data.local_database.entity.QuizSessionEntity

/**
 * Связанные данные: сессия и список ее результатов (вопросов).
 */
internal data class QuizSessionWithResultsTuple(
    @Embedded val session: QuizSessionEntity,
    @Relation(
        parentColumn = "sessionId",
        entityColumn = "sessionId"
    ) val results: List<QuizQuestionResultEntity>
)