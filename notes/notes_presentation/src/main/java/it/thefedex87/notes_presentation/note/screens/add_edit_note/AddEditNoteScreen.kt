package it.thefedex87.notes_presentation.note.screens.add_edit_note

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    title: String,
    onAddEditNoteEvent: (AddEditNoteEvent) -> Unit,
    snackbarHostState: SnackbarHostState,
    uiEvent: Flow<UiEvent>,
    note: TextFieldState,
    createdAt: LocalDateTime,
    currentMainScreenState: MainScreenState,
    onComposed: (MainScreenState) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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

                else -> Unit
            }
        }.launchIn(this)
    }

    LaunchedEffect(key1 = true) {
        onComposed(
            currentMainScreenState.copy(
                bottomBarVisible = false,
                topBarTitle = "",
                topBarActions = {
                    /*IconButton(onClick = {
                        onAddEditNoteEvent(
                            AddEditNoteEvent.OnSaveNoteClicked
                        )
                    }, modifier = Modifier.size(48.dp)) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = stringResource(id = R.string.save_note)
                        )
                    }*/
                }
            )
        )
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

    Box(modifier = modifier
        .fillMaxSize()
        .pointerInput(true) {
            awaitPointerEventScope {
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
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = spacing.spaceMedium, vertical = spacing.spaceSmall)
                .verticalScroll(rememberScrollState(), reverseScrolling = true)
        ) {
            BasicTextField2(
                value = title,
                onValueChange = {
                    onAddEditNoteEvent(AddEditNoteEvent.OnTitleChanged(it))
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorator = { innerText ->
                    Column {
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (!isFocused && title.isEmpty()) {
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
                state = note,
                lineLimits = TextFieldLineLimits.MultiLine(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                ),
                decorator = { innerText ->
                    if (note.text.isEmpty()) {
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun AddEditNoteScreenPreview() {
    AddEditNoteScreen(
        title = "Test",
        onAddEditNoteEvent = {},
        uiEvent = flow {  },
        snackbarHostState = SnackbarHostState(),
        note = TextFieldState(initialText = "Nota prova nota \n egfg"),
        createdAt = LocalDateTime.now(),
        currentMainScreenState = MainScreenState(),
        onComposed = {}
    )
}