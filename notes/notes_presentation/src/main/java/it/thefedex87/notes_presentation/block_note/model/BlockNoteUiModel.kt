package it.thefedex87.notes_presentation.block_note.model

import java.time.LocalDateTime

data class BlockNoteUiModel(
    val id: Long,
    val name: String,
    val color: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val showOptions: Boolean = false
)