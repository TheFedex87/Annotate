package it.thefedex87.alarms_domain.model

import java.time.LocalDateTime

data class AlarmDomainModel(
    val id: Long,
    val title: String,
    val time: LocalDateTime
)