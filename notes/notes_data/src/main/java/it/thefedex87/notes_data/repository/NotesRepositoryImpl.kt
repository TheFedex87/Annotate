package it.thefedex87.notes_data.repository

import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.entity.BlockNoteEntity
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.notes_domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.ZoneId

class NotesRepositoryImpl(
    val blockNoteDao: BlockNoteDao
): NotesRepository {


    override fun blockNotes(query: String): Flow<List<BlockNoteDomainModel>> = blockNoteDao.getBlockNotes(query).map {
        it.map {
            BlockNoteDomainModel(
                id = it.id,
                name = it.name,
                color = it.color,
                createdAt = Instant.ofEpochMilli(it.createdAt).atZone(ZoneId.systemDefault()).toLocalDate(),
                updatedAt = Instant.ofEpochMilli(it.updatedAt).atZone(ZoneId.systemDefault()).toLocalDate()
            )
        }
    }

    override suspend fun removeAllBlockNotes() {
        blockNoteDao.removeAllBlockNotes()
    }

    override suspend fun addBlockNote(blockNote: BlockNoteDomainModel) {
        blockNoteDao.insertBlockNote(
            BlockNoteEntity(
                id = blockNote.id,
                name = blockNote.name,
                color = blockNote.color,
                createdAt = blockNote.createdAt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                updatedAt = blockNote.updatedAt.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
            )
        )
    }
}