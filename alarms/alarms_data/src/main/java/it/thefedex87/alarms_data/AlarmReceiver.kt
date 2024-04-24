package it.thefedex87.alarms_data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
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
    }
}