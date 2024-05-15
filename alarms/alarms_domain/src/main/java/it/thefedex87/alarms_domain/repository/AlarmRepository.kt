package it.thefedex87.alarms_domain.repository

interface AlarmRepository {
    suspend fun setAlarmToNull(noteId: Long)
    suspend fun resetAlarms();
}