package it.thefedex87.alarms_presentation

sealed interface AlarmEvent {
    data class OnTimeClicked(val time: String) : AlarmEvent
    data class OnDateClicked(val time: String) : AlarmEvent
}