package ru.vafeen.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.domain.network.repository.QuizNetworkRepository
import ru.vafeen.domain.network.usecase.GetQuizUseCase

@Module
@InstallIn(ActivityRetainedComponent::class)
internal class UseCaseModule {
    @Provides
    fun provideGetQuizUseCase(quizNetworkRepository: QuizNetworkRepository): GetQuizUseCase =
        GetQuizUseCase(quizNetworkRepository = quizNetworkRepository)
}