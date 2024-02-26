package it.thefedex87.notes_presentation.block_note.addBlockNote

import androidx.compose.ui.graphics.Color

sealed interface AddBlockNoteEvent {
    data object OnConfirmClicked : AddBlockNoteEvent
    data object OnDismiss : AddBlockNoteEvent
    data class OnNameChanged(val name: String) : AddBlockNoteEvent
    data class OnSelectedNewColor(val color: Color): AddBlockNoteEvent
}