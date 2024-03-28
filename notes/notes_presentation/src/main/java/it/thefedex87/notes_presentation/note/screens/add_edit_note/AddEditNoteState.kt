package it.thefedex87.notes_presentation.note.screens.add_edit_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
data class AddEditNoteState(
    val noteId: Long? = null,
    val blockNoteId: Long = 0,
    val blockNoteName: String = "",
    val title: String = "",
    val note: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
)