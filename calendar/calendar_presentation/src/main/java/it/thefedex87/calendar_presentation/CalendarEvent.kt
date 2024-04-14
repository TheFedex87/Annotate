package it.thefedex87.calendar_presentation

import java.time.LocalDate

sealed interface CalendarEvent {
    data object OnPrevMonthClicked : CalendarEvent
    data object OnNextMonthClicked : CalendarEvent
    data class OnSelectedDayChanged(val day: LocalDate) : CalendarEvent
}