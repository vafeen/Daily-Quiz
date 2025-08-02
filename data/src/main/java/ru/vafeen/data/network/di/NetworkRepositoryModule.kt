package ru.vafeen.data.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.vafeen.data.network.repository.RetrofitQuizNetworkRepository
import ru.vafeen.domain.network.repository.QuizNetworkRepository

@Module
@InstallIn(ActivityComponent::class)
internal abstract class NetworkRepositoryModule {
    @Binds
    abstract fun bindsQuizNetworkRepository(
        retrofitQuizNetworkRepository: RetrofitQuizNetworkRepository
    ): QuizNetworkRepository
}