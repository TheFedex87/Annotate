package it.thefedex87.notes_data.mappers

import it.thefedex87.core.data.local.entity.NoteEntity
import it.thefedex87.core.domain.model.NoteDomainModel
import java.time.ZoneOffset

fun NoteDomainModel.toNoteEntity(): NoteEntity =
    NoteEntity(
        id = id,
        note = note,
        createdAt = createdAt.toEpochSecond(ZoneOffset.UTC),
        updatedAt = updatedAt.toEpochSecond(ZoneOffset.UTC),
        blockNoteId = blockNote.id!!
    )