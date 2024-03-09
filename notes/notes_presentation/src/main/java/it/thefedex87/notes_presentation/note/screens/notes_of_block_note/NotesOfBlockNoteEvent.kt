package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import it.thefedex87.core.domain.model.VisualizationType

sealed interface NotesOfBlockNoteEvent {
    data object OnAddNewNoteClicked : NotesOfBlockNoteEvent
    data class OnVisualizationTypeChanged(val visualizationType: VisualizationType): NotesOfBlockNoteEvent
}