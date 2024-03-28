package it.thefedex87.notes_presentation.note.model

import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.notes_presentation.block_note.model.toBlockNoteUiModel

fun NoteDomainModel.toNoteUiModel(isSelected: Boolean = false): NoteUiModel =
    NoteUiModel(
        id = id!!,
        title = title,
        body = note,
        createdAt = createdAt,
        updatedAt = updatedAt,
        blockNoteUiModel = blockNote.toBlockNoteUiModel(),
        isSelected = isSelected
    )