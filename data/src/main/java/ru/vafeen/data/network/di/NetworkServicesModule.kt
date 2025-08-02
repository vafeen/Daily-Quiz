package ru.vafeen.data.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vafeen.data.network.service.QuizService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkServicesModule {

    @Provides
    @Singleton
    fun provideQuizService(): QuizService = Retrofit
        .Builder()
        .baseUrl(QuizService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuizService::class.java)
}