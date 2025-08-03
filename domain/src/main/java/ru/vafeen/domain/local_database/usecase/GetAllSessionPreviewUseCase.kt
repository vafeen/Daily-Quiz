package ru.vafeen.domain.local_database.usecase

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository
import ru.vafeen.domain.models.QuizSessionResultPreview

class GetAllSessionPreviewUseCase(
    private val quizResultLocalRepository: QuizResultLocalRepository,

    ) {
    operator fun invoke(months: List<String>): Flow<List<QuizSessionResultPreview>> =
        quizResultLocalRepository.getAllSessionPreviews(months)
}