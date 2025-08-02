package ru.vafeen.data.local_database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository
import ru.vafeen.domain.local_database.usecase.SaveQuizSessionResultUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class DatabaseUseCaseModule {
    @Provides
    fun provideSaveQuizSessionResultUseCase(
        quizResultLocalRepository: QuizResultLocalRepository
    ): SaveQuizSessionResultUseCase = SaveQuizSessionResultUseCase(quizResultLocalRepository)
}