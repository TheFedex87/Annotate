package it.thefedex87.notes_presentation.note.model

import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel
import java.time.LocalDateTime

data class NoteUiModel(
    val id: Long,
    val title: String,
    val body: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val blockNoteUiModel: BlockNoteUiModel
)
