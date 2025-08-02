package ru.vafeen.domain.local_database.repository

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.models.QuizQuestion
import ru.vafeen.domain.models.QuizSessionPreview
import ru.vafeen.domain.models.QuizSessionResult

/**
 * Репозиторий локального хранения результатов викторины.
 *
 * Предоставляет методы для получения списка сессий (превью),
 * полноценного получения сессии с результатами, сохранения и удаления.
 */
interface QuizResultLocalRepository {

    /**
     * Получить поток всех сессий-вью без подробных результатов (превью).
     *
     * Используется для отображения краткой информации о сессиях,
     * например, в списках истории.
     *
     * @return [Flow] списка [QuizSessionPreview].
     */
    fun getAllSessionPreviews(): Flow<List<QuizSessionPreview>>

    /**
     * Получить полную сессию викторины с результатами по идентификатору.
     *
     * @param sessionId Идентификатор сессии в миллисекундах (метка времени).
     * @return Полная [QuizSessionResult] с вложенными результатами или null,
     * если сессия не найдена.
     */
    suspend fun getSessionById(sessionId: Long): QuizSessionResult?

    /**
     * Сохранить сессию викторины с результатами, формируя сущности из базовых данных.
     *
     * @param countOfRightAnswers Количество правильно отвеченных вопросов.
     * @param questions Список вопросов викторины.
     */
    suspend fun saveSession(countOfRightAnswers: Int, questions: List<QuizQuestion>)

    /**
     * Удалить сессию викторины по идентификатору.
     *
     * @param sessionId Идентификатор сессии для удаления.
     */
    suspend fun deleteSession(sessionId: Long)

    /**
     * Очистить все сохранённые сессии викторины из локального хранилища.
     */
    suspend fun clearAll()
}
