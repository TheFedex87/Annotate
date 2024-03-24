package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.DateOrderType
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.note.model.NoteUiModel

data class NotesOfBlockNoteState(
    val notes: List<NoteUiModel> = emptyList(),
    val visualizationType: VisualizationType = VisualizationType.Grid,
    val blockNote: BlockNoteDomainModel? = null,
    val orderBy: OrderBy = OrderBy.CreatedAt(DateOrderType.RECENT),
    val isOrderByExpanded: Boolean = false,
    val isMultiSelectionActive: Boolean = false
)
