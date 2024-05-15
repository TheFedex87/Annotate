package it.thefedex87.alarms_data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import it.thefedex87.alarms_domain.AlarmScheduler
import it.thefedex87.alarms_domain.repository.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class BootupReceiver: BroadcastReceiver() {
    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        CoroutineScope(Dispatchers.Main).launch {
            alarmRepository.resetAlarms()
        }
    }
}