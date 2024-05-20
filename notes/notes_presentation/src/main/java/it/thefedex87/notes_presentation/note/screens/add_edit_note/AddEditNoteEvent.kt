package it.thefedex87.notes_presentation.note.screens.add_edit_note

sealed interface AddEditNoteEvent {
    data class OnTitleChanged(val title: String): AddEditNoteEvent
    data class OnNoteChanged(val note: String): AddEditNoteEvent
    data object OnBackPressed : AddEditNoteEvent
    //data object OnSaveNoteClicked : AddEditNoteEvent

    data class OnSetAlarmClicked(val noteId: Long) : AddEditNoteEvent
    data object OnSetAlarmDismissed : AddEditNoteEvent
    data object OnTimeClicked : AddEditNoteEvent
    data object OnDateClicked : AddEditNoteEvent
    data class OnAlarmHourChanged(val hour: Int) : AddEditNoteEvent
    data class OnAlarmMinuteChanged(val minute: Int) : AddEditNoteEvent
    data class OnAlarmDateChanged(val date: Long) : AddEditNoteEvent
    data object OnAlarmTimePickerConfirmed : AddEditNoteEvent
    data object OnAlarmTimePickerCanceled : AddEditNoteEvent
    data object OnAlarmDatePickerConfirmed : AddEditNoteEvent
    data object OnAlarmDatePickerCanceled : AddEditNoteEvent
    data class OnAlarmEnabledCheckChanged(val enabled: Boolean) : AddEditNoteEvent
    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationPermissionRationale: Boolean
    ) : AddEditNoteEvent

    data object DismissRationaleDialog: AddEditNoteEvent
}