package it.thefedex87.notes_data.repository

import android.util.Log
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.NoteDao
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core.utils.Consts
import it.thefedex87.error_handling.DataError
import it.thefedex87.notes_data.mappers.toBlockNoteDomainModel
import it.thefedex87.notes_data.mappers.toBlockNoteEntity
import it.thefedex87.notes_data.mappers.toNoteDomainModel
import it.thefedex87.notes_data.mappers.toNoteEntity
import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import it.thefedex87.notes_domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import it.thefedex87.error_handling.Result
import it.thefedex87.logging.data.Logger
import kotlinx.coroutines.flow.first

class NotesRepositoryImpl(
    val blockNoteDao: BlockNoteDao,
    val noteDao: NoteDao,
    val notesPreferencesManager: NotesPreferencesManager,
    val logger: Logger
) : NotesRepository {
    override val notesPreferences: Flow<NotesPreferences>
        get() = notesPreferencesManager.preferencesFlow()

    override suspend fun updateBlockNotesVisualizationType(visualizationType: VisualizationType) {
        notesPreferencesManager.updateBlockNotesVisualizationType(visualizationType)
    }

    override fun blockNotes(query: String): Flow<List<BlockNoteDomainModel>> =
        blockNoteDao.getBlockNotes(query).map {
            it.map { blockNote ->
                blockNote.toBlockNoteDomainModel()
            }
        }

    override suspend fun removeAllBlockNotes() {
        blockNoteDao.removeAllBlockNotes()
    }

    override suspend fun addEditBlockNote(blockNote: BlockNoteDomainModel): Result<Long, DataError> {
        return try {
            val id = if (blockNote.id == null) {
                blockNoteDao.insertBlockNote(
                    blockNote.toBlockNoteEntity()
                )
            } else {
                blockNoteDao.updateBlockNote(
                    blockNote.toBlockNoteEntity()
                )
                blockNote.id!!
            }
            Result.Success(id)
        } catch (ex: Exception) {
            logger.d(Consts.TAG, "Error on saving BlockNote")
            Result.Error(DataError.Local.SAVE_INTO_DB_ERROR)
        }
    }

    override suspend fun removeBlockNote(blockNote: BlockNoteDomainModel): Result<Unit, DataError> {
        return try {
            blockNoteDao.removeBlockNote(blockNote.toBlockNoteEntity())
            Result.Success(Unit)
        } catch (ex: Exception) {
            logger.d(Consts.TAG, "Error removing BlockNote")
            Result.Error(DataError.Local.REMOVE_FROM_DB_ERROR)
        }
    }

    override fun notes(blockNote: BlockNoteDomainModel): Flow<List<NoteDomainModel>> {
        return noteDao.getNotesOfBlockNote(blockNote.id!!).map {
            it.map {
                it.toNoteDomainModel(blockNote)
            }
        }
    }

    override suspend fun getNote(
        id: Long,
        blockNote: BlockNoteDomainModel
    ): Result<NoteDomainModel, DataError.Local> {
        return try {
            val note = noteDao.getNote(id)
            Result.Success(note.toNoteDomainModel(blockNote))
        } catch (ex: Exception) {
            Result.Error(DataError.Local.NOT_FOUND)
        }
    }

    override suspend fun removeAllNotes(blockNoteId: Long) {
        noteDao.removeAllNotes(blockNoteId)
    }

    override suspend fun removeNotes(ids: List<Long>): Result<Unit, DataError> {
        return try {
            ids.forEach {
                noteDao.removeNote(it)
            }
            Result.Success(Unit)
        } catch (ex: Exception) {
            logger.d(Consts.TAG, "Error removing note")
            Result.Error(DataError.Local.REMOVE_FROM_DB_ERROR)
        }
    }

    override suspend fun addEditNote(note: NoteDomainModel): Result<Long, DataError.Local> {
        return try {
            val id = if (note.id == null) {
                val newId = noteDao.insertNote(
                    note.toNoteEntity()
                )
                newId
            } else {
                noteDao.updateNote(
                    note.toNoteEntity()
                )
                note.id!!
            }
            Result.Success(id)
        } catch (ex: Exception) {
            logger.d(Consts.TAG, "Error on saving Note")
            Result.Error(DataError.Local.SAVE_INTO_DB_ERROR)
        }
    }

    override suspend fun moveNotesToBlockNote(notes: List<Long>, blockNote: Long): Result<Unit, DataError> {
        return try {
            notes.forEach {
                noteDao.moveNote(it, blockNote)
            }

            Result.Success(Unit)
        } catch (ex: Exception) {
            Result.Error(DataError.Local.SAVE_INTO_DB_ERROR)
        }
    }

    override suspend fun removeNote(id: Long): Result<Unit, DataError.Local> {
        return try {
            noteDao.removeNote(id)
            Result.Success(Unit)
        } catch (ex: Exception) {
            logger.d(Consts.TAG, "Error on removing Note")
            Result.Error(DataError.Local.REMOVE_FROM_DB_ERROR)
        }
    }

    override suspend fun updateNotesVisualizationType(visualizationType: VisualizationType) {
        notesPreferencesManager.updateNotesVisualizationType(visualizationType)
    }

    override suspend fun updateNotesOrderBy(orderBy: OrderBy) {
        notesPreferencesManager.updateNotesOrderBy(orderBy)
    }

    override fun recentNotes(top: Int): Flow<List<NoteDomainModel>> {
        val blockNotes = mutableListOf<BlockNoteDomainModel>()
        return noteDao.getRecentUpdateNotes().map {
            it.map { note ->
                var blockNote = blockNotes.firstOrNull { it.id == note.id }
                if(blockNote == null) {
                    blockNote = blockNotes().first().first { it.id == note.blockNoteId }
                    blockNotes.add(blockNote)
                }
                note.toNoteDomainModel(blockNote)
            }
        }
    }
}