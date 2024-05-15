package it.thefedex87.alarms_data.repository

import it.thefedex87.alarms_domain.AlarmScheduler
import it.thefedex87.alarms_domain.model.AlarmDomainModel
import it.thefedex87.alarms_domain.repository.AlarmRepository
import it.thefedex87.core.data.local.AlarmDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao,
    private val alarmScheduler: AlarmScheduler
): AlarmRepository {
    override suspend fun setAlarmToNull(noteId: Long) {
        withContext(Dispatchers.IO) {
            alarmDao.removeAlarm(noteId)
        }
    }

    override suspend fun resetAlarms() {
        val notes = withContext(Dispatchers.IO) {
            alarmDao.getAlarms()
        }
        println(notes)
        notes?.filter { it.alarmTime != null }?.forEach { note ->
            alarmScheduler.schedule(
                AlarmDomainModel(
                    id = note.id!!,
                    title = note.title,
                    time = LocalDateTime.ofEpochSecond(note.alarmTime!!, 0, ZoneOffset.UTC)
                )
            )
        }
    }
}