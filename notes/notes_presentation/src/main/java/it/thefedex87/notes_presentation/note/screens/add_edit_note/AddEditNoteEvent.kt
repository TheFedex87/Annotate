package it.thefedex87.notes_presentation.note.screens.add_edit_note

sealed interface AddEditNoteEvent {
    data class OnTitleChanged(val title: String): AddEditNoteEvent
    //data object OnSaveNoteClicked : AddEditNoteEvent
}