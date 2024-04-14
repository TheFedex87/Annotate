package it.thefedex87.calendar_presentation

import java.time.LocalDate
import java.time.YearMonth

data class CalendarState(
    val days: List<LocalDate> = emptyList(),
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDay: LocalDate = LocalDate.now()
)