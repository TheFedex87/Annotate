package it.thefedex87.annotate.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.thefedex87.core.data.local.AnnotateDatabase
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.NoteDao
import it.thefedex87.core.utils.Consts
import it.thefedex87.logging.data.Logger
import it.thefedex87.logging.domain.LoggerLog
import it.thefedex87.notes_data.preferences.DefaultNotesPreferencesManager
import it.thefedex87.notes_data.repository.NotesRepositoryImpl
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import it.thefedex87.notes_domain.repository.NotesRepository
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
        annotateDb.blockNoteDao

    @Singleton
    @Provides
    fun provideNoteDao(annotateDb: AnnotateDatabase): NoteDao =
        annotateDb.noteDao

    @Singleton
    @Provides
    fun provideNotesPreferencesManager(@ApplicationContext context: Context): NotesPreferencesManager =
        DefaultNotesPreferencesManager(context)

    @Singleton
    @Provides
    fun provideNotesRepository(
        blockNoteDao: BlockNoteDao,
        noteDao: NoteDao,
        notesPreferencesManager: NotesPreferencesManager,
        logger: Logger
    ): NotesRepository =
        NotesRepositoryImpl(
            blockNoteDao = blockNoteDao,
            noteDao = noteDao,
            notesPreferencesManager = notesPreferencesManager,
            logger = logger
        )

    @Singleton
    @Provides
    fun provideLogger(): Logger =
        LoggerLog()
}