package it.thefedex87.notes_data.repository

import androidx.datastore.preferences.preferencesDataStore
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.entity.BlockNoteEntity
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_data.mappers.toBlockNoteModel
import it.thefedex87.notes_domain.model.BlockNoteModel
import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import it.thefedex87.notes_domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId

class NotesRepositoryImpl(
    val blockNoteDao: BlockNoteDao,
    val notesPreferencesManager: NotesPreferencesManager
): NotesRepository {
    override val notesPreferences: Flow<NotesPreferences>
        get() = notesPreferencesManager.preferencesFlow()

    override suspend fun updateBlockNotesVisualizationType(visualizationType: VisualizationType) {
        notesPreferencesManager.updateBlockNotesVisualizationType(visualizationType)
    }

    override fun blockNotes(query: String): Flow<List<BlockNoteModel>> = blockNoteDao.getBlockNotes(query).map {
        it.map {
            BlockNoteDomainModel(
                id = it.id,
                name = it.name,
                color = it.color,
                createdAt = Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate(),
                updatedAt = Instant.ofEpochMilli(it.updatedAt).atZone(ZoneId.systemDefault()).toLocalDate()
            ).toBlockNoteModel()
        }
    }

    override suspend fun removeAllBlockNotes() {
        blockNoteDao.removeAllBlockNotes()
    }

    override suspend fun addBlockNote(blockNote: BlockNoteDomainModel) {
        blockNoteDao.insertBlockNote(
            BlockNoteEntity(
                id = blockNote.id,
                name = blockNote.name,
                color = blockNote.color,
                createdAt = blockNote.createdAt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                updatedAt = blockNote.updatedAt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            )
        )
    }
}