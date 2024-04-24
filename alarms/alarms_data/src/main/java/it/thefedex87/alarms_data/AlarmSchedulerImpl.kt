package it.thefedex87.alarms_data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import it.thefedex87.alarms_domain.AlarmScheduler
import it.thefedex87.alarms_domain.model.AlarmDomainModel
import java.time.ZoneId

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(alarm: AlarmDomainModel) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("NOTE_TITLE", alarm.title)
            putExtra("NOTE_ID", alarm.id)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarm.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
            PendingIntent.getBroadcast(
                context,
                alarm.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(alarm: AlarmDomainModel) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarm.id.toInt(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}