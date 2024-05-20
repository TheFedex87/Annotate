package it.thefedex87.notes_presentation.note.screens.add_edit_note

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import it.thefedex87.alarms_presentation.AlarmView
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    state: AddEditNoteState,
    noteId: Long?,
    onAddEditNoteEvent: (AddEditNoteEvent) -> Unit,
    parentBlockNoteName: String,
    snackbarHostState: SnackbarHostState?,
    uiEvent: Flow<UiEvent>,
    createdAt: LocalDateTime,
    currentMainScreenState: MainScreenState,
    onComposed: (MainScreenState) -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val permissionLanucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            val activity = context as ComponentActivity
            val hasNotificationPermission = granted
            onAddEditNoteEvent(
                AddEditNoteEvent.SubmitNotificationPermissionInfo(
                    showNotificationPermissionRationale = Build.VERSION.SDK_INT >= 33 &&
                            shouldShowRequestPermissionRationale(
                                activity,
                                Manifest.permission.POST_NOTIFICATIONS
                            ),
                    acceptedNotificationPermission = hasNotificationPermission
                )
            )
        })

    LaunchedEffect(key1 = true) {
        uiEvent.onEach {
            Log.d(Consts.TAG, "Received new ui event in BlockNotesView: $it")
            when (it) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState?.showSnackbar(
                        message = it.message.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }

                is UiEvent.PopBackStack -> {
                    navHostController.popBackStack()
                }

                else -> Unit
            }
        }.launchIn(this)


        val activity = context as ComponentActivity
        val showNotificationRationale = Build.VERSION.SDK_INT >= 33 &&
                shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
        onAddEditNoteEvent(
            AddEditNoteEvent.SubmitNotificationPermissionInfo(
                showNotificationPermissionRationale = Build.VERSION.SDK_INT >= 33 &&
                        shouldShowRequestPermissionRationale(
                            activity,
                            Manifest.permission.POST_NOTIFICATIONS
                        ),
                acceptedNotificationPermission = Build.VERSION.SDK_INT >= 33 || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        )

        if (!showNotificationRationale) {
            permissionLanucher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    LaunchedEffect(key1 = parentBlockNoteName, key2 = state.canEnableAlarm) {
        onComposed(
            currentMainScreenState.copy(
                bottomBarVisible = false,
                topBarTitle = parentBlockNoteName,
                topBarActions = {
                    IconButton(
                        enabled = state.canEnableAlarm,
                        onClick = {
                            onAddEditNoteEvent(AddEditNoteEvent.OnSetAlarmClicked(noteId!!))
                        }) {
                        Icon(imageVector = Icons.Default.Alarm, contentDescription = null)
                    }
                },
                topBarBackPressed = {
                    onAddEditNoteEvent(AddEditNoteEvent.OnBackPressed)
                }
            )
        )
    }

    BackHandler {
        onAddEditNoteEvent(AddEditNoteEvent.OnBackPressed)
    }

    val createdAtStr = remember(createdAt) {
        "${createdAt.year}/${
            createdAt.monthValue.toString().padStart(2, '0')
        }/${createdAt.dayOfMonth.toString().padStart(2, '0')}    ${
            createdAt.hour.toString().padStart(2, '0')
        }:${createdAt.minute.toString().padStart(2, '0')}"
    }

    val spacing = LocalSpacing.current
    var isFocused by remember {
        mutableStateOf(false)
    }
    val focusRequester = remember {
        FocusRequester()
    }

    if (state.showAlarmDialog && !state.showTimePicker && !state.showDatePicker) {
        AlertDialog(onDismissRequest = {
            onAddEditNoteEvent(AddEditNoteEvent.OnSetAlarmDismissed)
        }) {
            Card {
                AlarmView(
                    onCheckedChanged = {
                        onAddEditNoteEvent(AddEditNoteEvent.OnAlarmEnabledCheckChanged(it))
                    },
                    enabled = state.isAlarmEnabled,
                    onTimeClicked = {
                        onAddEditNoteEvent(AddEditNoteEvent.OnTimeClicked)
                    },
                    onDateClicked = {
                        onAddEditNoteEvent(AddEditNoteEvent.OnDateClicked)
                    },
                    year = state.selectedAlarmYear,
                    month = state.selectedAlarmMonth,
                    day = state.selectedAlarmDay,
                    hour = state.selectedAlarmHour,
                    minute = state.selectedAlarmMinute
                )
            }
        }
    }


    Box(modifier = modifier
        .fillMaxSize()
        .pointerInput(true) {
            /*awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    // handle pointer event
                    //if (filter == null || event.type == filter) {
                    Log.d(Consts.TAG, "${event.type}, ${event.changes.first().position}")
                    //}
                    if (event.type == PointerEventType.Press) {
                        //focusRequester.requestFocus()
                    }
                }
            }*/
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
                .verticalScroll(rememberScrollState(), reverseScrolling = true)
        ) {
            BasicTextField2(
                value = state.title,
                onValueChange = {
                    onAddEditNoteEvent(AddEditNoteEvent.OnTitleChanged(it))
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorator = { innerText ->
                    Column {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (!isFocused && state.title.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.title_placeholder),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.Gray
                                    )
                                )
                            }
                            innerText()
                        }

                        Divider(
                            modifier = Modifier
                                .width(200.dp)
                                .padding(vertical = spacing.spaceExtraSmall),
                            thickness = 1.dp,
                            color = if (isFocused) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline
                        )
                    }
                },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.onFocusChanged {
                    isFocused = it.isFocused
                }
            )
            Text(
                text = createdAtStr,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            BasicTextField2(
                value = state.note,
                onValueChange = {
                    onAddEditNoteEvent(AddEditNoteEvent.OnNoteChanged(it))
                },
                lineLimits = TextFieldLineLimits.MultiLine(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                ),
                decorator = { innerText ->
                    if (state.note.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.note_placeholder),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        )
                    }
                    innerText()
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
        }
    }

    if (state.showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialMinute = state.selectedAlarmMinute,
            initialHour = state.selectedAlarmHour,
            is24Hour = true
        )
        LaunchedEffect(key1 = timePickerState.hour) {
            onAddEditNoteEvent(AddEditNoteEvent.OnAlarmHourChanged(timePickerState.hour))
        }
        LaunchedEffect(key1 = timePickerState.minute) {
            onAddEditNoteEvent(AddEditNoteEvent.OnAlarmMinuteChanged(timePickerState.minute))
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TimePicker(
                    state = timePickerState
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        onAddEditNoteEvent(AddEditNoteEvent.OnAlarmTimePickerConfirmed)
                    }) {
                        Text(text = stringResource(id = it.thefedex87.core_ui.R.string.confirm))
                    }
                    Spacer(modifier = Modifier.padding(spacing.spaceSmall))
                    TextButton(onClick = {
                        onAddEditNoteEvent(AddEditNoteEvent.OnAlarmTimePickerCanceled)
                    }) {
                        Text(text = stringResource(id = it.thefedex87.core_ui.R.string.cancel))
                    }
                }
            }
        }
    } else if (state.showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000,
            initialDisplayedMonthMillis = null,
            yearRange = 1970..2100,
            initialDisplayMode = DisplayMode.Picker
        )
        LaunchedEffect(key1 = datePickerState.selectedDateMillis) {
            onAddEditNoteEvent(AddEditNoteEvent.OnAlarmDateChanged(datePickerState.selectedDateMillis!!))
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DatePicker(
                    state = datePickerState
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        onAddEditNoteEvent(AddEditNoteEvent.OnAlarmDatePickerConfirmed)
                    }) {
                        Text(text = stringResource(id = it.thefedex87.core_ui.R.string.confirm))
                    }
                    Spacer(modifier = Modifier.padding(spacing.spaceSmall))
                    TextButton(onClick = {
                        onAddEditNoteEvent(AddEditNoteEvent.OnAlarmDatePickerCanceled)
                    }) {
                        Text(text = stringResource(id = it.thefedex87.core_ui.R.string.cancel))
                    }
                }
            }
        }
    }

    if (state.showNotificationRationale) {
        AlertDialog(onDismissRequest = { }) {
            Card {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.notification_permissions),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(id = R.string.notification_permission_rationale),
                        modifier = Modifier.padding(16.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clickable {
                                onAddEditNoteEvent(AddEditNoteEvent.DismissRationaleDialog)
                                permissionLanucher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.okay))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun AddEditNoteScreenPreview() {
    AddEditNoteScreen(
        state = AddEditNoteState(title = "Title", note = "Note"),
        noteId = 1,
        onAddEditNoteEvent = {},
        parentBlockNoteName = "Default",
        uiEvent = flow { },
        snackbarHostState = SnackbarHostState(),
        createdAt = LocalDateTime.now(),
        currentMainScreenState = MainScreenState(),
        navHostController = NavHostController(LocalContext.current),
        onComposed = {}
    )
}