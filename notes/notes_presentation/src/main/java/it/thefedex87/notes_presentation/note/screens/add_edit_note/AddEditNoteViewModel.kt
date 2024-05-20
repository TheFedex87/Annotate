package it.thefedex87.notes_presentation.note.screens.add_edit_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.alarms_domain.AlarmScheduler
import it.thefedex87.alarms_domain.model.AlarmDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.utils.UiText
import it.thefedex87.core_ui.utils.asErrorUiText
import it.thefedex87.error_handling.Result.Error
import it.thefedex87.error_handling.Result.Success
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_presentation.R
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _uiEvents = Channel<UiEvent>()
    val uiEvent = _uiEvents.receiveAsFlow()

    private var saveDebounceJob: Job? = null

    private val _state = MutableStateFlow(
        AddEditNoteState(
            /*note = TextFieldState(
                initialText = savedStateHandle[NotesConsts.ADD_EDIT_NOTE_BODY_SAVED_STATE_HANDLE_KEY]
                    ?: ""
            ),*/
            blockNoteId = savedStateHandle[NotesConsts.BLOCK_NOTE_ID]!!,
            noteId = savedStateHandle[NotesConsts.NOTE_ID]
        )
    )

    val state = _state.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        AddEditNoteState()
    )

    init {
        _state.value.noteId?.let { noteId ->
            viewModelScope.launch {
                if (noteId > 0) {
                    repository.blockNotes().first()
                        .firstOrNull { it.id == _state.value.blockNoteId }
                        ?.let { blocknote ->
                            when (val note = repository.getNote(noteId, blocknote)) {
                                is Error -> {
                                    _uiEvents.send(UiEvent.ShowSnackBar(note.asErrorUiText()))
                                }

                                is Success -> {
                                    _state.update {
                                        it.copy(
                                            createdAt = note.data.createdAt,
                                            blockNoteName = blocknote.name,
                                            note = note.data.note,
                                            title = note.data.title,
                                            canEnableAlarm = true,
                                            isAlarmEnabled = note.data.alarmTime != null,
                                            selectedAlarmYear = if(note.data.alarmTime?.year != null) note.data.alarmTime!!.year else it.selectedAlarmYear,
                                            selectedAlarmMonth = if(note.data.alarmTime?.month != null) note.data.alarmTime!!.monthValue else it.selectedAlarmMonth,
                                            selectedAlarmDay = if(note.data.alarmTime?.dayOfMonth != null) note.data.alarmTime!!.dayOfMonth else it.selectedAlarmDay,
                                            selectedAlarmHour = if(note.data.alarmTime?.hour != null) note.data.alarmTime!!.hour else it.selectedAlarmHour,
                                            selectedAlarmMinute = if(note.data.alarmTime?.minute != null) note.data.alarmTime!!.minute else it.selectedAlarmMinute,
                                        )
                                    }
                                }
                            }
                        }
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        viewModelScope.launch {
            when (event) {
                is AddEditNoteEvent.OnTitleChanged -> {
                    _state.update {
                        it.copy(
                            title = event.title
                        )
                    }
                    saveDebounceJob?.cancel()
                    saveDebounceJob = launch {
                        delay(500)
                        storeNote()
                    }
                }

                is AddEditNoteEvent.OnNoteChanged -> {
                    _state.update {
                        it.copy(
                            note = event.note
                        )
                    }
                    saveDebounceJob?.cancel()
                    saveDebounceJob = launch {
                        delay(500)
                        storeNote()
                    }
                }

                is AddEditNoteEvent.OnBackPressed -> {
                    val noteId = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)
                    if (noteId != null && _state.value.note.isEmpty() && _state.value.title.isEmpty()) {
                        when (val result = repository.removeNote(noteId)) {
                            is Error -> {
                                _uiEvents.send(
                                    UiEvent.ShowSnackBar(
                                        result.asErrorUiText()
                                    )
                                )
                            }

                            is Success -> {
                                _uiEvents.send(UiEvent.PopBackStack())
                            }
                        }
                    } else {
                        _uiEvents.send(UiEvent.PopBackStack())
                    }
                }

                is AddEditNoteEvent.OnSetAlarmClicked -> {
                    _state.update {
                        it.copy(
                            showAlarmDialog = true
                        )
                    }
                }

                is AddEditNoteEvent.OnTimeClicked -> {
                    _state.update {
                        it.copy(
                            showTimePicker = true
                        )
                    }
                }

                is AddEditNoteEvent.OnDateClicked ->{
                    _state.update {
                        it.copy(
                            showDatePicker = true
                        )
                    }
                }

                is AddEditNoteEvent.OnAlarmHourChanged -> {
                    _state.update {
                        it.copy(
                            selectedAlarmHourTmp = event.hour
                        )
                    }
                }

                is AddEditNoteEvent.OnAlarmMinuteChanged -> {
                    _state.update {
                        it.copy(
                            selectedAlarmMinuteTmp = event.minute
                        )
                    }
                }

                is AddEditNoteEvent.OnAlarmDateChanged -> {
                    _state.update {
                        it.copy(
                            selectedAlarmDateTmp = event.date
                        )
                    }
                }

                is AddEditNoteEvent.OnAlarmTimePickerCanceled -> {
                    _state.update {
                        it.copy(
                            showTimePicker = false
                        )
                    }
                }

                is AddEditNoteEvent.OnAlarmTimePickerConfirmed -> {
                    val alarmTime = LocalDateTime.of(
                        _state.value.selectedAlarmYear,
                        _state.value.selectedAlarmMonth,
                        _state.value.selectedAlarmDay,
                        _state.value.selectedAlarmHourTmp,
                        _state.value.selectedAlarmMinuteTmp
                    )
                    if(alarmTime < LocalDateTime.now() && _state.value.isAlarmEnabled) {
                        _uiEvents.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.no_alarm_past)))
                    } else {
                        _state.update {
                            it.copy(
                                showTimePicker = false,
                                selectedAlarmMinute = _state.value.selectedAlarmMinuteTmp,
                                selectedAlarmHour = _state.value.selectedAlarmHourTmp
                            )
                        }
                        storeNote()
                        if(_state.value.isAlarmEnabled) {
                            alarmScheduler.schedule(
                                AlarmDomainModel(
                                    id = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)!!,
                                    title = _state.value.title,
                                    time = alarmTime
                                )
                            )
                        }
                    }
                }

                is AddEditNoteEvent.OnAlarmDatePickerConfirmed -> {
                    val alarmDate = LocalDateTime.ofEpochSecond(_state.value.selectedAlarmDateTmp / 1000,0, ZoneOffset.UTC)
                    val alarmTime = LocalDateTime.of(
                        alarmDate.year,
                        alarmDate.month,
                        alarmDate.dayOfMonth,
                        _state.value.selectedAlarmHour,
                        _state.value.selectedAlarmMinute
                    )
                    if(alarmTime < LocalDateTime.now() && _state.value.isAlarmEnabled) {
                        _uiEvents.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.no_alarm_past)))
                    } else {
                        _state.update {
                            it.copy(
                                showDatePicker = false,
                                selectedAlarmYear = alarmDate.year,
                                selectedAlarmMonth = alarmDate.monthValue,
                                selectedAlarmDay = alarmDate.dayOfMonth
                            )
                        }
                        storeNote()
                        if(_state.value.isAlarmEnabled) {
                            alarmScheduler.schedule(
                                AlarmDomainModel(
                                    id = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)!!,
                                    title = _state.value.title,
                                    time = alarmTime
                                )
                            )
                        }
                    }
                }

                is AddEditNoteEvent.OnAlarmDatePickerCanceled -> {
                    _state.update {
                        it.copy(
                            showDatePicker = false
                        )
                    }
                }

                is AddEditNoteEvent.OnAlarmEnabledCheckChanged -> {
                    val alarmTime = LocalDateTime.of(
                        _state.value.selectedAlarmYear,
                        _state.value.selectedAlarmMonth,
                        _state.value.selectedAlarmDay,
                        _state.value.selectedAlarmHour,
                        _state.value.selectedAlarmMinute
                    )
                    if(alarmTime < LocalDateTime.now() && event.enabled) {
                        _uiEvents.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.no_alarm_past)))
                    } else {
                        _state.update {
                            it.copy(isAlarmEnabled = event.enabled)
                        }
                        storeNote()
                        if(event.enabled) {
                            alarmScheduler.schedule(
                                AlarmDomainModel(
                                    id = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)!!,
                                    title = _state.value.title,
                                    time = alarmTime
                                )
                            )
                        } else {
                            alarmScheduler.cancel(
                                AlarmDomainModel(
                                    id = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)!!,
                                    title = _state.value.title,
                                    time = alarmTime
                                )
                            )
                        }
                    }
                }

                is AddEditNoteEvent.OnSetAlarmDismissed -> {
                    _state.update {
                        it.copy(showAlarmDialog = false)
                    }
                }

                is AddEditNoteEvent.SubmitNotificationPermissionInfo -> {
                    _state.update {
                        it.copy(showNotificationRationale = event.showNotificationPermissionRationale)
                    }
                }

                is AddEditNoteEvent.DismissRationaleDialog -> {
                    _state.update {
                        it.copy(showNotificationRationale = false)
                    }
                }
            }
        }
    }

    private suspend fun storeNote() {
        val currentId = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)
        if (_state.value.note != "" ||
            _state.value.title != ""
        ) {
            repository.blockNotes().first().firstOrNull { it.id == _state.value.blockNoteId }
                ?.let { blocknote ->
                    val result = repository.addEditNote(
                        NoteDomainModel(
                            id = if (currentId == 0L) null else currentId,
                            title = _state.value.title,
                            note = _state.value.note,
                            blockNote = blocknote,
                            createdAt = _state.value.createdAt,
                            alarmTime = if (_state.value.isAlarmEnabled)
                                LocalDateTime.of(
                                    _state.value.selectedAlarmYear,
                                    _state.value.selectedAlarmMonth,
                                    _state.value.selectedAlarmDay,
                                    _state.value.selectedAlarmHour,
                                    _state.value.selectedAlarmMinute
                                ) else null,
                            updatedAt = LocalDateTime.now()
                        )
                    )
                    when (result) {
                        is Error -> {
                            _uiEvents.send(
                                UiEvent.ShowSnackBar(
                                    result.asErrorUiText()
                                )
                            )
                        }

                        is Success -> {
                            savedStateHandle[NotesConsts.NOTE_ID] = result.data
                            _state.update {
                                it.copy(
                                    canEnableAlarm = true
                                )
                            }

                        }
                    }
                }
        } else {
            /*currentId?.let {
                if (it > 0) {
                    when (val result = repository.removeNote(it)) {
                        is Error -> {
                            _uiEvents.send(
                                UiEvent.ShowSnackBar(
                                    result.asErrorUiText()
                                )
                            )
                        }

                        is Success -> Unit
                    }
                }
            }
            savedStateHandle.remove<Long>(NotesConsts.NOTE_ID)*/
        }
    }
}