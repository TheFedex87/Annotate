package it.thefedex87.notes_data.mappers

import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.notes_domain.model.BlockNoteModel

fun BlockNoteDomainModel.toBlockNoteModel(): BlockNoteModel = BlockNoteModel(
    id = id,
    name = name,
    color = color,
    createdAt = createdAt,
    updatedAt = updatedAt
)