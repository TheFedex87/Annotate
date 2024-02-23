package it.thefedex87.notes_presentation.block_note.addBlockNote

sealed interface AddBlockNoteEvent {
    data object OnConfirmClicked : AddBlockNoteEvent
    data object OnDismiss : AddBlockNoteEvent
    data class OnNameChanged(val name: String) : AddBlockNoteEvent
}