package it.thefedex87.core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.thefedex87.core.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM NoteEntity WHERE blockNoteId = :blockNoteId")
    fun getNotesOfBlockNote(blockNoteId: Long): Flow<List<NoteEntity>>

    @Query("SELECT * FROM NoteEntity ORDER BY updatedAt")
    fun getRecentUpdateNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM NoteEntity WHERE blockNoteId == :blockNoteId")
    suspend fun removeAllNotes(blockNoteId: Long)
}