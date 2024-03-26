package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType

sealed interface NotesOfBlockNoteEvent {
    data class OnAddNewNoteClicked(val blockNoteId: Long) : NotesOfBlockNoteEvent
    data class OnVisualizationTypeChanged(val visualizationType: VisualizationType) :
        NotesOfBlockNoteEvent

    data class OnNoteClicked(val id: Long, val blockNoteId: Long) : NotesOfBlockNoteEvent

    data class OnOrderByChanged(val orderBy: OrderBy) : NotesOfBlockNoteEvent
    data class ExpandOrderByMenuChanged(val isExpanded: Boolean) : NotesOfBlockNoteEvent
    data class MultiSelectionStateChanged(val active: Boolean, val id: Long) : NotesOfBlockNoteEvent
    data object DeselectAllNotes : NotesOfBlockNoteEvent
    data class OnSelectionChanged(val id: Long, val selected: Boolean): NotesOfBlockNoteEvent
    data object OnRemoveSelectedNotesClicked : NotesOfBlockNoteEvent
    data object OnRemoveSelectedNotesConfirmed : NotesOfBlockNoteEvent
    data object OnRemoveSelectedNotesCanceled : NotesOfBlockNoteEvent

    // Move notes to another block note events
    data object OnMoveNotesRequested : NotesOfBlockNoteEvent
    data object OnMoveNotesCanceled : NotesOfBlockNoteEvent
    data object OnMoveNotesConfirmed : NotesOfBlockNoteEvent
    data class OnMoveNotesNewBlockNoteSelected(val id: Long) : NotesOfBlockNoteEvent
    data class ExpandMoveNotesBlockNotesList(val isExpanded: Boolean) : NotesOfBlockNoteEvent
}