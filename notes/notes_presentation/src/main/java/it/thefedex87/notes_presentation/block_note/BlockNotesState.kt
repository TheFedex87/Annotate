package it.thefedex87.notes_presentation.block_note

import androidx.compose.runtime.Immutable
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.VisualizationType

@Immutable
data class BlockNotesState(
    val blockNotes: List<BlockNoteDomainModel> = emptyList(),
    val isLoading: Boolean = false,
    val visualizationType: VisualizationType = VisualizationType.Grid,
    val userInputTest: String = ""
)