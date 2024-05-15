package it.thefedex87.alarms_data

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import it.thefedex87.alarms_domain.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver: BroadcastReceiver() {
    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("NOTE_TITLE")
        val id = intent!!.getLongExtra("NOTE_ID", 0)
        println("Received alarm of note: $title")
        /*CoroutineScope(Dispatchers.Main).launch {
            alarmRepository.setAlarmToNull(id)
        }*/
        getNotification(title, context!!)
    }

    @SuppressLint("MissingPermission")
    private fun getNotification(title: String?, context: Context) {
        createNotificationChannel(context)
        val builder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.baseline_note_24)
            .setContentTitle("Annotate")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {

            // notificationId is a unique int for each notification that you must define.
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ANNOTATE_ALARM"
            val descriptionText = "Annotations alarms"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}