package it.thefedex87.notes_data.repository

import android.util.Log
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.NoteDao
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core.utils.Consts
import it.thefedex87.notes_data.mappers.toBlockNoteDomainModel
import it.thefedex87.notes_data.mappers.toBlockNoteEntity
import it.thefedex87.notes_data.mappers.toNoteDomainModel
import it.thefedex87.notes_data.mappers.toNoteEntity
import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import it.thefedex87.notes_domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset

class NotesRepositoryImpl(
    val blockNoteDao: BlockNoteDao,
    val noteDao: NoteDao,
    val notesPreferencesManager: NotesPreferencesManager
): NotesRepository {
    override val notesPreferences: Flow<NotesPreferences>
        get() = notesPreferencesManager.preferencesFlow()

    override suspend fun updateBlockNotesVisualizationType(visualizationType: VisualizationType) {
        notesPreferencesManager.updateBlockNotesVisualizationType(visualizationType)
    }

    override fun blockNotes(query: String): Flow<List<BlockNoteDomainModel>> = blockNoteDao.getBlockNotes(query).map {
        it.map { blockNote ->
            blockNote.toBlockNoteDomainModel()
        }
    }

    override suspend fun removeAllBlockNotes() {
        blockNoteDao.removeAllBlockNotes()
    }

    override suspend fun addEditBlockNote(blockNote: BlockNoteDomainModel) {
        try {
            if (blockNote.id == null) {
                blockNoteDao.insertBlockNote(
                    blockNote.toBlockNoteEntity()
                )
            } else {
                blockNoteDao.updateBlockNote(
                    blockNote.toBlockNoteEntity()
                )
            }
        }
        catch (ex: Exception) {
            Log.d(Consts.TAG, "Error on saving BlockNote")
            throw ex
        }
    }

    override suspend fun removeBlockNote(blockNote: BlockNoteDomainModel) {
        try {
            blockNoteDao.removeBlockNote(blockNote.toBlockNoteEntity())
        }
        catch (ex: Exception) {
            Log.d(Consts.TAG, "Error removing BlockNote")
            throw ex
        }
    }

    override fun notes(blockNote: BlockNoteDomainModel): Flow<List<NoteDomainModel>> {
        return noteDao.getNotesOfBlockNote(blockNote.id!!).map {
            it.map {
                it.toNoteDomainModel(blockNote)
            }
        }
    }

    override suspend fun getNote(id: Long, blockNote: BlockNoteDomainModel): NoteDomainModel {
        val note = noteDao.getNote(id)
        return note.toNoteDomainModel(blockNote)
    }

    override suspend fun removeAllNotes(blockNoteId: Long) {
        noteDao.removeAllNotes(blockNoteId)
    }

    override suspend fun addEditNote(note: NoteDomainModel): Long {
        return try {
            if (note.id == null) {
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
        } catch (ex: Exception) {
            Log.d(Consts.TAG, "Error on saving Note")
            throw ex
        }
    }

    override suspend fun removeNote(id: Long) {
        try {
            noteDao.removeNote(id)
        } catch (ex: Exception) {
            Log.d(Consts.TAG, "Error on removing Note")
            throw ex
        }
    }

    override suspend fun updateNotesVisualizationType(visualizationType: VisualizationType) {
        notesPreferencesManager.updateNotesVisualizationType(visualizationType)
    }

    override suspend fun updateNotesOrderBy(orderBy: OrderBy) {
        notesPreferencesManager.updateNotesOrderBy(orderBy)
    }
}