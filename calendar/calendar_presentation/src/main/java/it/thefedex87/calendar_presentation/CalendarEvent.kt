package it.thefedex87.calendar_presentation

import java.time.LocalDate
import java.time.YearMonth

sealed interface CalendarEvent {
    data object OnPrevClicked : CalendarEvent
    data object OnNextClicked : CalendarEvent
    data class OnSelectedDayChanged(val day: LocalDate) : CalendarEvent
    data object OnYearClicked : CalendarEvent
    data class OnMonthClicked(val month: YearMonth) : CalendarEvent
}