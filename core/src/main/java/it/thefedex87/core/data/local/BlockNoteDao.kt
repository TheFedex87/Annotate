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
    @Query("SELECT * FROM BlockNoteEntity")
    fun getBlockNotes(): Flow<BlockNoteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBlockNote(blockNote: BlockNoteEntity): Long

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun updateBlockNote(blockNote: BlockNoteEntity)

    @Delete
    fun removeBlockNote(blockNote: BlockNoteEntity)
}