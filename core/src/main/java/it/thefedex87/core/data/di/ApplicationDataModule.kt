package it.thefedex87.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.thefedex87.core.data.repository.AnnotateRepositoryImpl
import it.thefedex87.core.domain.repository.AnnotateRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationDataModule {

    @Singleton
    @Provides
    fun provideAnnotateRepository(): AnnotateRepository =
        AnnotateRepositoryImpl()
}