package it.thefedex87.alarms_domain

import it.thefedex87.alarms_domain.model.AlarmDomainModel

interface AlarmScheduler {
    fun schedule(alarm: AlarmDomainModel)
    fun cancel(alarm: AlarmDomainModel)
}