package it.thefedex87.notes_presentation.note.screens.add_edit_note

import androidx.compose.foundation.ExperimentalFoundationApi
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalFoundationApi::class)
data class AddEditNoteState(
    val noteId: Long? = null,
    val blockNoteId: Long = 0,
    val blockNoteName: String = "",
    val title: String = "",
    val note: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val showAlarmDialog: Boolean = false,
    val showTimePicker: Boolean = false,
    val showDatePicker: Boolean = false,
    val selectedAlarmHourTmp: Int = LocalDateTime.now().hour,
    val selectedAlarmMinuteTmp: Int = LocalDateTime.now().minute,
    val selectedAlarmDateTmp: Long = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    val selectedAlarmHour: Int = LocalDateTime.now().plusHours(1).hour,
    val selectedAlarmMinute: Int = LocalDateTime.now().plusHours(1).minute,
    val selectedAlarmYear: Int = LocalDateTime.now().plusHours(1).year,
    val selectedAlarmMonth: Int = LocalDateTime.now().plusHours(1).monthValue,
    val selectedAlarmDay: Int = LocalDateTime.now().plusHours(1).dayOfMonth,

    val canEnableAlarm: Boolean = false,
    val isAlarmEnabled: Boolean = false,

    )