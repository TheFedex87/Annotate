package it.thefedex87.notes_domain.repository

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_domain.model.NotesPreferences
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    val notesPreferences: Flow<NotesPreferences>

    suspend fun updateBlockNotesVisualizationType(visualizationType: VisualizationType)
    fun blockNotes(query: String = ""): Flow<List<BlockNoteDomainModel>>
    suspend fun addEditBlockNote(blockNote: BlockNoteDomainModel)
    suspend fun removeBlockNote(blockNote: BlockNoteDomainModel)
    suspend fun removeAllBlockNotes()

    suspend fun updateNotesVisualizationType(visualizationType: VisualizationType)
    fun notes(blockNote: BlockNoteDomainModel): Flow<List<NoteDomainModel>>
    suspend fun getNote(id: Long, blockNote: BlockNoteDomainModel): NoteDomainModel
    suspend fun removeAllNotes(blockNoteId: Long)
    suspend fun removeNote(id: Long)
    suspend fun addEditNote(note: NoteDomainModel): Long
    suspend fun updateNotesOrderBy(orderBy: OrderBy)
}