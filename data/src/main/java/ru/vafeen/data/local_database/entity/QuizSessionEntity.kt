package ru.vafeen.data.local_database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Сущность сессии викторины для хранения результатов.
 *
 * @property sessionId Идентификатор сессии, равный миллисекундам от epoch (dateTime).
 * @property dateTime Дата и время сессии.
 * @property name Имя сессии или пользователя.
 * @property countOfRightAnswers Количество правильных ответов.
 */
@Entity(tableName = "quiz_sessions")
internal data class QuizSessionEntity(
    @PrimaryKey(autoGenerate = true) val sessionId: Long = 0,
    val dateTime: LocalDateTime,
    val name: String,
    val countOfRightAnswers: Int
)
