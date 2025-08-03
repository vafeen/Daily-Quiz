package ru.vafeen.data.local_database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.local_database.converter.toDomain
import ru.vafeen.data.local_database.converter.toDomainPreview
import ru.vafeen.data.local_database.converter.toEntity
import ru.vafeen.data.local_database.dao.QuizDao
import ru.vafeen.data.local_database.entity.QuizSessionEntity
import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.models.QuizSessionPreview
import ru.vafeen.domain.models.QuizSessionResult
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Реализация локального репозитория результатов викторины на основе Room.
 *
 * Использует [QuizDao] для прямого взаимодействия с базой данных.
 *
 * @property quizDao DAO для доступа к базе данных.
 */
internal class RoomQuizResultLocalRepository @Inject constructor(
    private val quizDao: QuizDao
) : QuizResultLocalRepository {

    /**
     * Получить поток со списком превью всех сессий из базы данных.
     *
     * @param months Список названий месяцев (индексируется с 0 — январь) для форматирования даты.
     * @return [Flow] списка [QuizSessionPreview].
     */
    override fun getAllSessionPreviews(months: List<String>): Flow<List<QuizSessionPreview>> =
        quizDao.getAllSessions()
            .map { list -> list.map { it.toDomainPreview(months) } }

    /**
     * Получить полную сессию викторины с результатами по идентификатору.
     *
     * @param sessionId Идентификатор сессии.
     * @return [QuizSessionResult] с вложенными результатами или null, если сессия не найдена.
     */
    override suspend fun getSessionById(sessionId: Long): QuizSessionResult? =
        quizDao.getSessionWithResults(sessionId)?.toDomain()

    /**
     * Сохранить сессию викторины, выполняя следующие шаги:
     * 1. Вставить сессию с пустым именем для автогенерации ID.
     * 2. Обновить имя сессии на "Quiz $sessionId".
     * 3. Вставить связанные вопросы с установленным sessionId.
     *
     * @param countOfRightAnswers Количество правильных ответов в сессии.
     * @param questions Список вопросов викторины.
     */
    override suspend fun saveSession(countOfRightAnswers: Int, questions: List<QuizQuestion>) {
        val now = LocalDateTime.now()

        // 1. Вставка сессии с пустым именем
        val sessionEntity = QuizSessionEntity(
            sessionId = 0,  // 0 для автогенерации
            dateTime = now,
            name = "",
            countOfRightAnswers = countOfRightAnswers
        )
        val generatedSessionId = quizDao.insertSession(sessionEntity)

        // 2. Обновление имени сессии по сгенерированному ID
        quizDao.updateSessionName(generatedSessionId, "Quiz $generatedSessionId")

        // 3. Вставка вопросов с правильным sessionId
        val questionEntities = questions.map { question ->
            question.toEntity(generatedSessionId)
        }
        quizDao.insertQuestions(questionEntities)
    }

    /**
     * Удалить сессию викторины по идентификатору.
     *
     * @param sessionId Идентификатор сессии для удаления.
     */
    override suspend fun deleteSession(sessionId: Long) =
        quizDao.deleteSession(sessionId)

    /**
     * Очистить все сессии викторины из базы данных.
     */
    override suspend fun clearAll() =
        quizDao.clearSessions()
}
