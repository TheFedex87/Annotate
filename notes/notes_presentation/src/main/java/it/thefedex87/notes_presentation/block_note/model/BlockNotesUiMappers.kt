package it.thefedex87.notes_presentation.block_note.model

import it.thefedex87.core.domain.model.BlockNoteDomainModel

fun BlockNoteDomainModel.toBlockNoteUiModel(showOptions: Boolean = false) = BlockNoteUiModel(
    id = id!!,
    name = name,
    color = color,
    createdAt = createdAt,
    updatedAt = updatedAt,
    showOptions = showOptions
)

fun BlockNoteUiModel.toBlockNoteDomainModel() = BlockNoteDomainModel(
    id = id,
    name = name,
    color = color,
    createdAt = createdAt,
    updatedAt = updatedAt
)