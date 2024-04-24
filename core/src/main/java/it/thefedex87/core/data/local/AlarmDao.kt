package it.thefedex87.core.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AlarmDao {
    @Query("UPDATE NoteEntity SET alarmTime = NULL WHERE id = :noteId")
    fun removeAlarm(noteId: Long)
}