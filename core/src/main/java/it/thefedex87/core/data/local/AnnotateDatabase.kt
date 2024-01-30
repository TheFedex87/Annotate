package it.thefedex87.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.thefedex87.core.data.local.entity.BlockNoteEntity

@Database(
    entities = [
        BlockNoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AnnotateDatabase: RoomDatabase() {
    abstract val blockNotesDao: BlockNoteDao
}