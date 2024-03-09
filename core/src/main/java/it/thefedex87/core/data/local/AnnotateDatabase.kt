package it.thefedex87.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.thefedex87.core.data.local.entity.BlockNoteEntity
import it.thefedex87.core.data.local.entity.NoteEntity

@Database(
    entities = [
        BlockNoteEntity::class,
        NoteEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AnnotateDatabase: RoomDatabase() {
    abstract val blockNoteDao: BlockNoteDao
    abstract val noteDao: NoteDao
}