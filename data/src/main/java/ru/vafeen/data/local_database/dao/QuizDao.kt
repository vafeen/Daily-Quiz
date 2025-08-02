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

@Dao
internal interface QuizDao {
    @Transaction
    @Query("SELECT * FROM quiz_sessions WHERE sessionId = :sessionId")
    suspend fun getSessionWithResults(sessionId: Long): QuizSessionWithResultsTuple?

    @Query("SELECT sessionId, dateTime, name, countOfRightAnswers FROM quiz_sessions ORDER BY dateTime DESC")
    fun getAllSessionPreviews(): Flow<List<QuizSessionPreviewTuple>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: QuizSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(results: List<QuizQuestionResultEntity>)

    @Transaction
    suspend fun insertSessionWithResults(
        session: QuizSessionEntity,
        results: List<QuizQuestionResultEntity>
    ) {
        insertSession(session)
        insertQuestions(results)
    }

    @Query("DELETE FROM quiz_sessions WHERE sessionId = :sessionId")
    suspend fun deleteSession(sessionId: Long)

    @Query("SELECT * FROM quiz_sessions ORDER BY dateTime DESC")
    fun getAllSessions(): Flow<List<QuizSessionEntity>>

    @Query("DELETE FROM quiz_sessions")
    suspend fun clearSessions()

}
