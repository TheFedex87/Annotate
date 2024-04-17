package it.thefedex87.calendar_presentation

import java.time.LocalDate
import java.time.YearMonth

data class CalendarState(
    val days: List<LocalDate> = emptyList(),
    val currentYear: Int = YearMonth.now().year,
    val currentMonth: YearMonth? = YearMonth.now(),
    val selectedDay: LocalDate = LocalDate.now(),
    val currentView: ViewType = ViewType.MONTH
)

enum class ViewType {
    MONTH,
    YEAR
}