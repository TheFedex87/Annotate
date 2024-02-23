package it.thefedex87.notes_presentation.block_note

import it.thefedex87.core.domain.model.VisualizationType

sealed interface BlockNotesEvent {
    data class OnBlockNoteClicked(val id: Long): BlockNotesEvent
    //data class OnQueryChanged(val query: String): BlockNotesEvent
    data class OnVisualizationTypeChanged(val visualizationType: VisualizationType): BlockNotesEvent
    object OnAddNewBlockNoteClicked : BlockNotesEvent
}