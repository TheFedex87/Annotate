package it.thefedex87.notes_presentation.block_note.model

import java.time.LocalDate

data class BlockNoteUiModel(
    val id: Long,
    val name: String,
    val color: Int,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)