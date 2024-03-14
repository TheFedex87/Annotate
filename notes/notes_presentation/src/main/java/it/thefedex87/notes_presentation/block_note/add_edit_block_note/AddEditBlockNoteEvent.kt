package it.thefedex87.notes_presentation.block_note.add_edit_block_note

import androidx.compose.ui.graphics.Color

sealed interface AddEditBlockNoteEvent {
    data object OnConfirmClicked : AddEditBlockNoteEvent
    data object OnDismiss : AddEditBlockNoteEvent
    data class OnNameChanged(val name: String) : AddEditBlockNoteEvent
    data class OnSelectedNewColor(val color: Color): AddEditBlockNoteEvent
}