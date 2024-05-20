package it.thefedex87.search.data

import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.NoteDao
import it.thefedex87.core.data.mappers.toBlockNoteDomainModel
import it.thefedex87.core.data.mappers.toNoteDomainModel
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.error_handling.DataError
import it.thefedex87.error_handling.Result
import it.thefedex87.search.domain.SearchRepository
import kotlinx.coroutines.flow.first

class SearchRepositoryImpl(
    private val blockNoteDao: BlockNoteDao,
    private val noteDao: NoteDao
): SearchRepository {
    override suspend fun filterBlockNotes(query: String): Result<List<BlockNoteDomainModel>, DataError> {
        return try {
            val blockNotes =  blockNoteDao.getBlockNotes(query).first().map {
                it.toBlockNoteDomainModel()
            }
            Result.Success(blockNotes)
        } catch (ex: Exception) {
            Result.Error(DataError.Local.UNEXPECTED)
        }
    }

    override suspend fun filterNotes(query: String): Result<List<NoteDomainModel>, DataError> {
        return try {
            val notes =  noteDao.filterNote(query).first().map { note ->
                val blockNote = blockNoteDao.getBlockNotes("").first().firstOrNull {
                    it.id == note.id
                }?.toBlockNoteDomainModel() ?: return Result.Error(DataError.Local.NOT_FOUND)

                note.toNoteDomainModel(blockNote)
            }
            Result.Success(notes)
        } catch (ex: Exception) {
            Result.Error(DataError.Local.UNEXPECTED)
        }
    }
}