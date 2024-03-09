package it.thefedex87.notes_data.mappers

import it.thefedex87.core.data.local.entity.BlockNoteEntity
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import java.time.LocalDateTime
import java.time.ZoneOffset

fun BlockNoteDomainModel.toBlockNoteEntity(): BlockNoteEntity =
    BlockNoteEntity(
        id = id,
        name = name,
        color = color,
        createdAt = createdAt.toEpochSecond(ZoneOffset.UTC),
        updatedAt = updatedAt.toEpochSecond(ZoneOffset.UTC),
    )

fun BlockNoteEntity.toBlockNoteDomainModel(): BlockNoteDomainModel =
    BlockNoteDomainModel(
        id = id,
        name = name,
        color = color,
        createdAt = LocalDateTime.ofEpochSecond(createdAt, 0, ZoneOffset.UTC),
        updatedAt = LocalDateTime.ofEpochSecond(updatedAt, 0, ZoneOffset.UTC)
    )