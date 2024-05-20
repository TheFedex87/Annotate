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

    @Query("SELECT * FROM NoteEntity ORDER BY updatedAt DESC")
    fun getRecentUpdateNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM NoteEntity WHERE title LIKE '%' || :query || '%' OR body LIKE '%' || :query || '%'")
    fun filterNote(query: String): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Query("SELECT * FROM NoteEntity WHERE id = :id")
    suspend fun getNote(id: Long): NoteEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM NoteEntity WHERE blockNoteId == :blockNoteId")
    suspend fun removeAllNotes(blockNoteId: Long)

    @Query("DELETE FROM NoteEntity WHERE id == :id")
    suspend fun removeNote(id: Long)

    @Query("UPDATE NoteEntity SET blockNoteId = :blockNoteId WHERE id = :id")
    suspend fun moveNote(id: Long, blockNoteId: Long)
}