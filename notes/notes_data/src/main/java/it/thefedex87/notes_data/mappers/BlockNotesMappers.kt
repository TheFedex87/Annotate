package it.thefedex87.notes_data.mappers

import it.thefedex87.core.data.local.entity.BlockNoteEntity
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import java.time.ZoneOffset

fun BlockNoteDomainModel.toBlockNoteEntity(): BlockNoteEntity =
    BlockNoteEntity(
        id = id,
        name = name,
        color = color,
        createdAt = createdAt.toEpochSecond(ZoneOffset.UTC),
        updatedAt = updatedAt.toEpochSecond(ZoneOffset.UTC),
    )