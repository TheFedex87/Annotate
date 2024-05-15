package it.thefedex87.core.data.local

import androidx.room.Dao
import androidx.room.Query
import it.thefedex87.core.data.local.entity.NoteEntity

@Dao
interface AlarmDao {
    @Query("UPDATE NoteEntity SET alarmTime = NULL WHERE id = :noteId")
    fun removeAlarm(noteId: Long)

    @Query("SELECT * FROM NoteEntity WHERE alarmTime IS NOT NULL")
    fun getAlarms(): List<NoteEntity>?
}