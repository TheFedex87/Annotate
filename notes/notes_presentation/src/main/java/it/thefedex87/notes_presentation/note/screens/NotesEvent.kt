package it.thefedex87.notes_presentation.note.screens

import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType

sealed interface NotesEvent {
    data class OnAddNewNoteClicked(val blockNoteId: Long) : NotesEvent
    data class OnVisualizationTypeChanged(val visualizationType: VisualizationType) :
        NotesEvent

    data class OnNoteClicked(val id: Long, val blockNoteId: Long) : NotesEvent

    data class OnOrderByChanged(val orderBy: OrderBy) : NotesEvent
    data class ExpandOrderByMenuChanged(val isExpanded: Boolean) : NotesEvent
    data class MultiSelectionStateChanged(val active: Boolean, val id: Long) : NotesEvent
    data object DeselectAllNotes : NotesEvent
    data class OnSelectionChanged(val id: Long, val selected: Boolean): NotesEvent
    data object OnRemoveSelectedNotesClicked : NotesEvent
    data object OnRemoveSelectedNotesConfirmed : NotesEvent
    data object OnRemoveSelectedNotesCanceled : NotesEvent

    // Move notes to another block note events
    data object OnMoveNotesRequested : NotesEvent
    data object OnMoveNotesCanceled : NotesEvent
    data object OnMoveNotesConfirmed : NotesEvent
    data class OnMoveNotesNewBlockNoteSelected(val id: Long) : NotesEvent
    data class ExpandMoveNotesBlockNotesList(val isExpanded: Boolean) : NotesEvent
}