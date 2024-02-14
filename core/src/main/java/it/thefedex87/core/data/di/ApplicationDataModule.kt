package it.thefedex87.core.data.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.thefedex87.core.data.local.AnnotateDatabase
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.repository.AnnotateRepositoryImpl
import it.thefedex87.core.domain.repository.AnnotateRepository
import it.thefedex87.utils.Consts
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationDataModule {

    @Singleton
    @Provides
    fun provideAnnotateDatabase(app: Application): AnnotateDatabase =
        Room.databaseBuilder(
            app,
            AnnotateDatabase::class.java,
            Consts.DB_NAME
        ).build()

    @Singleton
    @Provides
    fun provideBlockNoteDao(annotateDb: AnnotateDatabase): BlockNoteDao =
        annotateDb.blockNotesDao

    @Singleton
    @Provides
    fun provideAnnotateRepository(blockNoteDao: BlockNoteDao): AnnotateRepository =
        AnnotateRepositoryImpl(blockNoteDao)
}