package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType

sealed interface NotesOfBlockNoteEvent {
    data class OnAddNewNoteClicked(val blockNoteId: Long) : NotesOfBlockNoteEvent
    data class OnVisualizationTypeChanged(val visualizationType: VisualizationType) :
        NotesOfBlockNoteEvent

    data class OnNoteClicked(val id: Long) : NotesOfBlockNoteEvent

    data class OnOrderByChanged(val orderBy: OrderBy) :NotesOfBlockNoteEvent
    data class ExpandOrderByMenuChanged(val isExpanded: Boolean) : NotesOfBlockNoteEvent
}