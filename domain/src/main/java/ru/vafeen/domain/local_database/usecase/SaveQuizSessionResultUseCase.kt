package ru.vafeen.domain.local_database.usecase

import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository
import ru.vafeen.domain.models.QuizQuestion

/**
 * Use case для сохранения результата сессии викторины в локальное хранилище.
 *
 * Обеспечивает сохранение сессии викторины на основе количества правильных ответов
 * и списка вопросов через [QuizResultLocalRepository].
 *
 * @property quizResultLocalRepository Репозиторий для работы с результатами викторины в локальном хранилище.
 */
class SaveQuizSessionResultUseCase(
    private val quizResultLocalRepository: QuizResultLocalRepository,
) {
    /**
     * Сохраняет результат сессии викторины.
     *
     * @param countOfRightAnswers Количество правильно отвеченных вопросов.
     * @param questions Список вопросов викторины.
     */
    suspend operator fun invoke(countOfRightAnswers: Int, questions: List<QuizQuestion>) =
        quizResultLocalRepository.saveSession(countOfRightAnswers, questions)
}