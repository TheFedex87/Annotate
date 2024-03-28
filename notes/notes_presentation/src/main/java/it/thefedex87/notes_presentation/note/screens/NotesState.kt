package it.thefedex87.notes_presentation.note.screens

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.DateOrderType
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel
import it.thefedex87.notes_presentation.note.model.NoteUiModel

data class NotesState(
    val notes: List<NoteUiModel> = emptyList(),
    val visualizationType: VisualizationType = VisualizationType.Grid,
    val blockNote: BlockNoteDomainModel? = null,
    val orderBy: OrderBy = OrderBy.CreatedAt(DateOrderType.RECENT),
    val isOrderByExpanded: Boolean = false,
    val isMultiSelectionActive: Boolean = false,
    val showConfirmDeleteDialog: Boolean = false,
    val showOrderByCombo: Boolean = false,

    // Move of notes to another blocknote states
    val showMoveNotesDialog: Boolean = false,
    val moveNotesBlockNotesExpanded: Boolean = false,
    val selectedBlockNoteToMoveNotes: Long? = null,
    val availableBlockNotes: List<BlockNoteUiModel> = emptyList()
)
