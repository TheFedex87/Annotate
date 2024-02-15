package it.thefedex87.notes_presentation.block_note

sealed interface BlockNotesEvent {
    data class OnBlockNoteClicked(val id: Long): BlockNotesEvent
    data class OnQueryChanged(val query: String): BlockNotesEvent
}