package it.thefedex87.notes_data.mappers

import it.thefedex87.core.data.local.entity.NoteEntity
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import java.time.LocalDateTime
import java.time.ZoneOffset

fun NoteDomainModel.toNoteEntity(): NoteEntity =
    NoteEntity(
        id = id,
        title = title,
        body = note,
        createdAt = createdAt.toEpochSecond(ZoneOffset.UTC),
        updatedAt = updatedAt.toEpochSecond(ZoneOffset.UTC),
        blockNoteId = blockNote.id!!
    )

fun NoteEntity.toNoteDomainModel(blockNote: BlockNoteDomainModel): NoteDomainModel =
    NoteDomainModel(
        id = id,
        title = title,
        note = body,
        createdAt = LocalDateTime.ofEpochSecond(createdAt, 0, ZoneOffset.UTC),
        updatedAt = LocalDateTime.ofEpochSecond(updatedAt, 0, ZoneOffset.UTC),
        blockNote = blockNote
    )