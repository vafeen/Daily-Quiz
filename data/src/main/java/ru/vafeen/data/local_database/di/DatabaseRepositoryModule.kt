package ru.vafeen.data.local_database.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ru.vafeen.data.local_database.repository.RoomQuizResultLocalRepository
import ru.vafeen.domain.local_database.repository.QuizResultLocalRepository

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class DatabaseRepositoryModule {
    @Binds
    abstract fun bindsQuizResultLocalRepository(
        roomQuizResultLocalRepository: RoomQuizResultLocalRepository
    ): QuizResultLocalRepository
}
