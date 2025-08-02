package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Сущность результата отдельного вопроса викторины, связанная с сессией.
 *
 * @property id Уникальный ID результата вопроса.
 * @property sessionId Внешний ключ к сессии (QuizSessionEntity.sessionId).
 * @property question Текст вопроса.
 * @property category Категория вопроса.
 * @property difficulty Сложность вопроса.
 * @property correctAnswer Правильный ответ.
 * @property allAnswers Список всех вариантов ответов.
 * @property chosenAnswer Выбранный пользователем ответ
 */
@Entity(
    tableName = "quiz_question_results",
    foreignKeys = [
        ForeignKey(
            entity = QuizSessionEntity::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId")]
)
internal data class QuizQuestionResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val question: String,
    val category: String,
    val difficulty: String,
    val correctAnswer: String,
    val allAnswers: List<String>,
    val chosenAnswer: String,
)