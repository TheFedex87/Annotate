package it.thefedex87.core.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import it.thefedex87.core.data.local.entity.BlockNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockNoteDao {
    @Query("SELECT * FROM BlockNoteEntity WHERE name LIKE '%' || :query || '%'")
    fun getBlockNotes(query: String): Flow<List<BlockNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockNote(blockNote: BlockNoteEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBlockNote(blockNote: BlockNoteEntity)

    @Delete
    suspend fun removeBlockNote(blockNote: BlockNoteEntity)

    @Query("DELETE FROM BlockNoteEntity")
    suspend fun removeAllBlockNotes()
}