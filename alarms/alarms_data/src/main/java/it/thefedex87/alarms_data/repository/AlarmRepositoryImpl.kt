package it.thefedex87.alarms_data.repository

import it.thefedex87.alarms_domain.repository.AlarmRepository
import it.thefedex87.core.data.local.AlarmDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao
): AlarmRepository {
    override suspend fun setAlarmToNull(noteId: Long) {
        withContext(Dispatchers.IO) {
            alarmDao.removeAlarm(noteId)
        }
    }
}