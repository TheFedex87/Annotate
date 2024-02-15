package it.thefedex87.notes_domain.repository

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    //val appPreferences: Flow<AnnotatePreferences>

    fun blockNotes(query: String = ""): Flow<List<BlockNoteDomainModel>>
    suspend fun addBlockNote(blockNote: BlockNoteDomainModel)
    suspend fun removeAllBlockNotes()
}