package ru.vafeen.domain.local_database.usecase

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository
import ru.vafeen.domain.models.QuizSessionResultPreview

/**
 * Use case для получения списка превью сессий викторины из локального хранилища.
 *
 * Получает поток списка превью сессий, используя репозиторий [QuizResultLocalRepository].
 *
 * @property quizResultLocalRepository Репозиторий для работы с результатами викторины в локальном хранилище.
 */
class GetAllSessionPreviewUseCase(
    private val quizResultLocalRepository: QuizResultLocalRepository,
) {
    /**
     * Возвращает поток списка превью сессий викторины.
     *
     * @param months Список названий месяцев для фильтрации превью сессий.
     * @return Поток, содержащий список превью сессий викторины.
     */
    operator fun invoke(months: List<String>): Flow<List<QuizSessionResultPreview>> =
        quizResultLocalRepository.getAllSessionPreviews(months)
}
