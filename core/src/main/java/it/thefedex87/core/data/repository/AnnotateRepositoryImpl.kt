package it.thefedex87.core.data.repository

import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.entity.BlockNoteEntity
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.repository.AnnotateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId

class AnnotateRepositoryImpl(
    val blockNoteDao: BlockNoteDao
): AnnotateRepository {
    /*override val blockNotes: Flow<List<BlockNoteDomainModel>>
        get() = flow {
            emit(
                listOf(
                    BlockNoteDomainModel(
                        id = 1,
                        name = "Default",
                        color = Color.argb(255, 0, 128, 0),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    ),
                    BlockNoteDomainModel(
                        id = 2,
                        name = "Blocco note 1",
                        color = Color.argb(255, 23, 128, 99),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    ),
                    BlockNoteDomainModel(
                        id = 3,
                        name = "Cucina",
                        color = Color.argb(255, 128, 128, 0),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    ),
                    BlockNoteDomainModel(
                        id = 4,
                        name = "Lavoro",
                        color = Color.argb(255, 145, 45, 100),
                        createdAt = LocalDate.of(2024, 1, 12),
                        updatedAt = LocalDate.of(2024, 1, 12),
                    )
                )
            )
        }*/

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