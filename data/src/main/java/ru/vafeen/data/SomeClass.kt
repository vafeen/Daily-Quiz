package ru.vafeen.data

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vafeen.domain.SomeInterface
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SomeModule {
    @Binds
    @Singleton
    abstract fun provideSomeClass(someClass: SomeClass): SomeInterface
}

internal class SomeClass @Inject constructor() : SomeInterface {
    override val someValue: Int = 5
}