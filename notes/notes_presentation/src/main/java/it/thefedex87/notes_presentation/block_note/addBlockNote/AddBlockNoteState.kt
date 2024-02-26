package it.thefedex87.notes_presentation.block_note.addBlockNote

import androidx.compose.ui.graphics.Color

data class AddBlockNoteState(
    val showDialog: Boolean = false,
    val name: String = "",
    val selectedColor: Color = Color.Magenta
)