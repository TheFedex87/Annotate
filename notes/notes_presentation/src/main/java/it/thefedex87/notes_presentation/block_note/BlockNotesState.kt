package it.thefedex87.notes_presentation.block_note

import androidx.compose.runtime.Immutable
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_domain.model.BlockNoteModel

@Immutable
data class BlockNotesState(
    val blockNotes: List<BlockNoteModel> = emptyList(),
    val isLoading: Boolean = false,
    val visualizationType: VisualizationType = VisualizationType.Grid
)