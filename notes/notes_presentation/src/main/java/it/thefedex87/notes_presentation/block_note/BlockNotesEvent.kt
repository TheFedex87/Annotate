package it.thefedex87.notes_presentation.block_note

import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel

sealed interface BlockNotesEvent {
    data class OnBlockNoteClicked(val blockNote: BlockNoteUiModel): BlockNotesEvent
    //data class OnQueryChanged(val query: String): BlockNotesEvent
    data class OnVisualizationTypeChanged(val visualizationType: VisualizationType): BlockNotesEvent
    data object OnAddNewBlockNoteClicked : BlockNotesEvent
    data class OnShowBlockNoteOptionsClicked(val id: Long): BlockNotesEvent
    data object OnDismissBlockNoteOptions: BlockNotesEvent
    data class OnEditBlockNoteClicked(val id: Long): BlockNotesEvent
    data class OnDeleteBlockNoteClicked(val id: Long): BlockNotesEvent
    data object OnDeleteBlockNoteConfirmed: BlockNotesEvent
    data object OnDeleteBlockNoteDismissed: BlockNotesEvent
}