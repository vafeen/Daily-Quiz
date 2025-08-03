package ru.vafeen.data.local_database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.local_database.entity.QuizQuestionResultEntity
import ru.vafeen.data.local_database.entity.QuizSessionEntity
import ru.vafeen.data.local_database.tuple.QuizSessionPreviewTuple
import ru.vafeen.data.local_database.tuple.QuizSessionWithResultsTuple

/**
 * DAO для работы с таблицами сессий викторины и результатов вопросов.
 */
@Dao
internal interface QuizDao {

    /**
     * Получить сессию викторины с вложенными результатами вопросов.
     *
     * @param sessionId Идентификатор сессии.
     * @return Кортеж с сессией и результатами или null, если сессия не найдена.
     */
    @Transaction
    @Query("SELECT * FROM quiz_sessions WHERE sessionId = :sessionId")
    suspend fun getSessionWithResults(sessionId: Long): QuizSessionWithResultsTuple?

    /**
     * Получить список превью всех сессий для отображения.
     *
     * Используется для отображения в списках, содержит только базовые поля.
     *
     * @return Flow со списком кортежей превью сессий.
     */
    @Query("SELECT sessionId, dateTime, name, countOfRightAnswers FROM quiz_sessions ORDER BY dateTime DESC")
    fun getAllSessionPreviews(): Flow<List<QuizSessionPreviewTuple>>

    /**
     * Вставить новую сессию викторины.
     *
     * @param session Сущность сессии (поле sessionId = 0 для автогенерации).
     * @return Сгенерированный Room идентификатор новой сессии.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: QuizSessionEntity): Long

    /**
     * Вставить список результатов вопросов викторины.
     *
     * @param results Список сущностей результатов вопросов.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(results: List<QuizQuestionResultEntity>)

    /**
     * Обновить имя сессии викторины.
     *
     * @param sessionId Идентификатор сессии.
     * @param name Новое имя сессии.
     */
    @Query("UPDATE quiz_sessions SET name = :name WHERE sessionId = :sessionId")
    suspend fun updateSessionName(sessionId: Long, name: String)

    /**
     * Удалить сессию викторины по идентификатору.
     *
     * @param sessionId Идентификатор сессии для удаления.
     */
    @Query("DELETE FROM quiz_sessions WHERE sessionId = :sessionId")
    suspend fun deleteSession(sessionId: Long)

    /**
     * Получить все сессии викторины (полные сущности).
     *
     * @return Flow со списком сущностей сессий.
     */
    @Query("SELECT * FROM quiz_sessions ORDER BY dateTime DESC")
    fun getAllSessions(): Flow<List<QuizSessionEntity>>

    /**
     * Удалить все сессии викторины и связанные вопросы.
     */
    @Query("DELETE FROM quiz_sessions")
    suspend fun clearSessions()

}
