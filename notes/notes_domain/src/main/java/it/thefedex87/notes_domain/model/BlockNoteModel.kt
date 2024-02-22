package it.thefedex87.notes_domain.model

import java.time.LocalDate

data class BlockNoteModel(
    val id: Long,
    val name: String,
    val color: Int,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
