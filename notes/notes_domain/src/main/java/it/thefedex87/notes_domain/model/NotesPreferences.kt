package it.thefedex87.notes_domain.model

import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType

data class NotesPreferences(
    val blockNotesVisualizationType: VisualizationType,
    val notesVisualizationType: VisualizationType,
    val notesOrderBy: OrderBy
)