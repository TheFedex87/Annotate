package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.note.model.NoteUiModel

data class NotesOfBlockNoteState(
    val notes: List<NoteUiModel> = emptyList(),
    val visualizationType: VisualizationType = VisualizationType.Grid,
    val blockNoteName: String = ""
)
