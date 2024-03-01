package it.thefedex87.notes_presentation.block_note.model

import java.time.LocalDateTime

data class NoteUiModel(
    val id: Long,
    val note: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val blockNoteUiModel: BlockNoteUiModel
)
