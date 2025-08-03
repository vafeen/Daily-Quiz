package ru.vafeen.domain.local_database.usecase

import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository

/**
 * Use case для получения результата сессии викторины из локального хранилища.
 *
 * Использует [QuizResultLocalRepository] для загрузки данных сессии по идентификатору.
 *
 * @property quizResultLocalRepository Репозиторий для работы с результатами викторины в локальном хранилище.
 */
class GetQuizSessionResultUseCase(
    private val quizResultLocalRepository: QuizResultLocalRepository,
) {
    /**
     * Возвращает результат сессии викторины по её идентификатору.
     *
     * @param sessionId Идентификатор сессии викторины.
     * @return Результат сессии викторины или null, если данных нет.
     */
    suspend operator fun invoke(sessionId: Long) =
        quizResultLocalRepository.getSessionById(sessionId)
}
