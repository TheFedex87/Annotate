package it.thefedex87.calendar_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    init {
        composeMonth(YearMonth.now())
    }

    private fun composeMonth(month: YearMonth) {
        val firstDayOfMonth = month.atDay(1)
        val days = mutableListOf<LocalDate>()
        val dayOfWeek = firstDayOfMonth.dayOfWeek

        for(i in dayOfWeek.value - 1 downTo 1) {
            days.add(firstDayOfMonth.minusDays(i.toLong()))
        }
        days.add(firstDayOfMonth)

        for(i in 1..<month.lengthOfMonth()) {
            days.add(firstDayOfMonth.plusDays(i.toLong()))
        }
        val lastDayOfMonth = days.last()
        for(i in 1..7 - lastDayOfMonth.dayOfWeek.value) {
            days.add(lastDayOfMonth.plusDays(i.toLong()))
        }

        _state.update {
            it.copy(
                days = days,
                currentYear = month.year,
                currentMonth = month,
                currentView = ViewType.MONTH
            )
        }
    }

    fun onEvent(event: CalendarEvent) {
        viewModelScope.launch {
            when(event) {
                is CalendarEvent.OnPrevClicked -> {
                    when(_state.value.currentView) {
                        ViewType.MONTH -> {
                            composeMonth(_state.value.currentMonth!!.minusMonths(1))
                        }
                        ViewType.YEAR -> {
                            _state.update {
                                it.copy(
                                    currentYear = _state.value.currentYear - 1
                                )
                            }
                        }
                    }
                }
                is CalendarEvent.OnNextClicked -> {
                    when(_state.value.currentView) {
                        ViewType.MONTH -> {
                            composeMonth(_state.value.currentMonth!!.plusMonths(1))
                        }
                        ViewType.YEAR -> {
                            _state.update {
                                it.copy(
                                    currentYear = _state.value.currentYear + 1
                                )
                            }
                        }
                    }
                }
                is CalendarEvent.OnSelectedDayChanged -> {
                    if(event.day.year != _state.value.currentMonth!!.year) {
                        if(event.day.year < _state.value.currentMonth!!.year) {
                            composeMonth(_state.value.currentMonth!!.minusMonths(1))
                        } else {
                            composeMonth(_state.value.currentMonth!!.plusMonths(1))
                        }
                    } else if (event.day.month != _state.value.currentMonth!!.month) {
                        if (event.day.month < _state.value.currentMonth!!.month || (event.day.month.value == 12 && _state.value.currentMonth!!.month.value == 1)) {
                            composeMonth(_state.value.currentMonth!!.minusMonths(1))
                        } else if(event.day.month > _state.value.currentMonth!!.month || (event.day.month.value == 1 && _state.value.currentMonth!!.month.value == 12)) {
                            composeMonth(_state.value.currentMonth!!.plusMonths(1))
                        }
                    }
                    _state.update {
                        it.copy(
                            selectedDay = event.day
                        )
                    }
                }
                is CalendarEvent.OnYearClicked -> {
                    _state.update {
                        it.copy(
                            currentView = ViewType.YEAR,
                            currentMonth = null
                        )
                    }
                }
                is CalendarEvent.OnMonthClicked -> {
                    composeMonth(
                        event.month
                    )
                }
            }
        }
    }
}