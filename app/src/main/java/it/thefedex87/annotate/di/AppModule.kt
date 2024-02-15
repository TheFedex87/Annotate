package it.thefedex87.annotate.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.thefedex87.core.data.local.AnnotateDatabase
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.notes_data.repository.NotesRepositoryImpl
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.utils.Consts
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
    fun provideNotesRepository(blockNoteDao: BlockNoteDao): NotesRepository =
        NotesRepositoryImpl(
            blockNoteDao = blockNoteDao
        )
}