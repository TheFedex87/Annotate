package it.thefedex87.alarms_presentation

import java.time.LocalDate

data class AlarmState(
    val time: String = "08:00",
    val date: String = LocalDate.now().toString(),
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false
)