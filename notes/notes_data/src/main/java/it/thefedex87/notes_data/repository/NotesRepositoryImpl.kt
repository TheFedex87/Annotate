package it.thefedex87.notes_data.repository

import android.util.Log
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.entity.BlockNoteEntity
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_data.mappers.toBlockNoteEntity
import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.utils.Consts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset

class NotesRepositoryImpl(
    val blockNoteDao: BlockNoteDao,
    val notesPreferencesManager: NotesPreferencesManager
): NotesRepository {
    override val notesPreferences: Flow<NotesPreferences>
        get() = notesPreferencesManager.preferencesFlow()

    override suspend fun updateBlockNotesVisualizationType(visualizationType: VisualizationType) {
        notesPreferencesManager.updateBlockNotesVisualizationType(visualizationType)
    }

    override fun blockNotes(query: String): Flow<List<BlockNoteDomainModel>> = blockNoteDao.getBlockNotes(query).map {
        it.map {
            BlockNoteDomainModel(
                id = it.id,
                name = it.name,
                color = it.color,
                createdAt = LocalDateTime.ofEpochSecond(it.createdAt, 0, ZoneOffset.UTC),
                updatedAt = LocalDateTime.ofEpochSecond(it.updatedAt, 0, ZoneOffset.UTC)
            )
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
}