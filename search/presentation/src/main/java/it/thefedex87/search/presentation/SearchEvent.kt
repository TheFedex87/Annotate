package it.thefedex87.search.presentation

sealed interface SearchEvent {
    data class OnNoteClicked(val blockNoteId: Long, val noteId: Long): SearchEvent
    data class OnBlockNoteClicked(val blockNoteId: Long): SearchEvent
}