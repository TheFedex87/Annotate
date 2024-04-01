package it.thefedex87.notes_presentation.block_note.repository

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.error_handling.DataError
import it.thefedex87.error_handling.Result
import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.notes_domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeNotesRepository: NotesRepository {
    private val _notesPreferences = MutableStateFlow(
        NotesPreferences(
            blockNotesVisualizationType = VisualizationType.Grid,
            notesVisualizationType = VisualizationType.Grid,
            notesOrderBy = OrderBy.Title
        )
    )

    private val _blockNotes = MutableStateFlow(listOf<BlockNoteDomainModel>())

    fun setBlockNotesList(blockNotes: List<BlockNoteDomainModel>) {
        _blockNotes.update {
            blockNotes
        }
    }

    override val notesPreferences: Flow<NotesPreferences>
        get() = _notesPreferences.asStateFlow()

    override suspend fun updateBlockNotesVisualizationType(visualizationType: VisualizationType) {
        _notesPreferences.update {
            it.copy(
                blockNotesVisualizationType = visualizationType
            )
        }
    }

    override fun blockNotes(query: String): Flow<List<BlockNoteDomainModel>> {
        return _blockNotes.asStateFlow()
    }

    override suspend fun addEditBlockNote(blockNote: BlockNoteDomainModel): Result<Long, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun removeBlockNote(blockNote: BlockNoteDomainModel): Result<Unit, DataError> {
        val newList = _blockNotes.value.toMutableList()
        newList.removeIf { blockNote.id == it.id }

        _blockNotes.update {
            newList
        }
        return Result.Success(Unit)
    }

    override suspend fun removeAllBlockNotes() {
        TODO("Not yet implemented")
    }

    override suspend fun updateNotesVisualizationType(visualizationType: VisualizationType) {
        TODO("Not yet implemented")
    }

    override fun notes(blockNote: BlockNoteDomainModel): Flow<List<NoteDomainModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun getNote(
        id: Long,
        blockNote: BlockNoteDomainModel
    ): Result<NoteDomainModel, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllNotes(blockNoteId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun removeNotes(ids: List<Long>): Result<Unit, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun removeNote(id: Long): Result<Unit, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun addEditNote(note: NoteDomainModel): Result<Long, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun moveNotesToBlockNote(
        notes: List<Long>,
        blockNote: Long
    ): Result<Unit, DataError> {
        TODO("Not yet implemented")
    }

    override suspend fun updateNotesOrderBy(orderBy: OrderBy) {
        TODO("Not yet implemented")
    }

    override fun recentNotes(top: Int): Flow<List<NoteDomainModel>> {
        TODO("Not yet implemented")
    }
}