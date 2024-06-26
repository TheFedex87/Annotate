package it.thefedex87.notes_presentation

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class NotesRepositoryFake : NotesRepository {
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

    private val _recentNotes = MutableStateFlow(listOf<NoteDomainModel>())
    fun setRecentNotes(notes: List<NoteDomainModel>) {
        _recentNotes.update {
            notes
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
        val existingBlockNote = _blockNotes.first().firstOrNull { it.id == blockNote.id }


        if (existingBlockNote == null) {
            val blockNoteWithId = blockNote.copy(
                id = _blockNotes.value.size.toLong()
            )
            val blockNotes = _blockNotes.value + blockNoteWithId
            _blockNotes.update {
                blockNotes
            }
        } else {
            val blockNotes = _blockNotes.value.toMutableList()
            blockNotes.removeIf { it.id == blockNote.id }
            blockNotes.add(blockNote)
            _blockNotes.update {
                blockNotes
            }
        }

        return Result.Success(_blockNotes.value.size.toLong())
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
        _notesPreferences.update {
            it.copy(
                notesVisualizationType = visualizationType
            )
        }
    }

    fun setNotes(notes: List<NoteDomainModel>) {
        _notes.update {
            notes
        }
    }
    private val _notes = MutableStateFlow(listOf<NoteDomainModel>())
    override fun notes(blockNote: BlockNoteDomainModel): Flow<List<NoteDomainModel>> =
        _notes.asStateFlow().map {
            it.filter {
                it.id == blockNote.id
            }
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
        _notesPreferences.update {
            it.copy(
                notesOrderBy = orderBy
            )
        }
    }

    override fun recentNotes(top: Int): Flow<List<NoteDomainModel>> = _recentNotes.asStateFlow()
}