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
import java.time.ZoneId
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
     * Для быстрого обращения без загрузки вложенных результатов вопросов.
     *
     * @return [Flow] списка [QuizSessionPreview].
     */
    override fun getAllSessionPreviews(): Flow<List<QuizSessionPreview>> =
        quizDao.getAllSessions()
            .map { list ->
                list.map {
                    it.toDomainPreview()
                }
            }

    /**
     * Получить полную сессию викторины с результатами по ID.
     *
     * @param sessionId Идентификатор сессии (миллисекунды).
     * @return [QuizSessionResult] с вложенными результатами или null, если не найдена.
     */
    override suspend fun getSessionById(sessionId: Long): QuizSessionResult? =
        quizDao.getSessionWithResults(sessionId)?.toDomain()

    /**
     * Сохранить сессию викторины, преобразуя входные параметры в сущности Room.
     *
     * @param countOfRightAnswers Количество правильных ответов в сессии.
     * @param questions Список вопросов викторины.
     */
    override suspend fun saveSession(countOfRightAnswers: Int, questions: List<QuizQuestion>) {
        val now = LocalDateTime.now()
        val sessionId = now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val sessionEntity = QuizSessionEntity(
            sessionId = sessionId,
            dateTime = now,
            name = "Quiz $now",
            countOfRightAnswers = countOfRightAnswers
        )

        val questionEntities = questions.map { question ->
            question.toEntity(sessionId)
        }

        quizDao.insertSessionWithResults(sessionEntity, questionEntities)
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
