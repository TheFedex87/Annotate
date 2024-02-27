package it.thefedex87.notes_presentation.block_note

import androidx.compose.runtime.Immutable
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.block_note.addBlockNote.AddBlockNoteState
import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel

@Immutable
data class BlockNotesState(
    val blockNotes: List<BlockNoteUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val visualizationType: VisualizationType = VisualizationType.Grid,
    val addBlockNoteState: AddBlockNoteState = AddBlockNoteState(),
    val showOptionsId: Long? = null,
    val blockNoteDeleteState: BlockNoteDeleteState = BlockNoteDeleteState()
)

@Immutable
data class BlockNoteDeleteState(
    val showDeleteDialog: Boolean = false,
    val deleteBlockNoteName: String = ""
)