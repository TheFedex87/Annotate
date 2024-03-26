package it.thefedex87.notes_presentation.note.screens.recent

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.thefedex87.core.domain.model.DateOrderType
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.components.SimpleDeleteDialog
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.notes_presentation.R
import it.thefedex87.notes_presentation.note.components.MoveNotesDialog
import it.thefedex87.notes_presentation.note.components.NotesList
import it.thefedex87.notes_presentation.note.screens.notes_of_block_note.NotesOfBlockNoteEvent
import it.thefedex87.notes_presentation.note.screens.notes_of_block_note.NotesOfBlockNoteState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentNotesScreen(
    state: RecentNotesState,
    onComposed: (MainScreenState) -> Unit,
    uiEvent: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState?,
    currentMainScreenState: MainScreenState,
    onNotesEvent: (NotesOfBlockNoteEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(enabled = state.isMultiSelectionActive) {
        onNotesEvent(NotesOfBlockNoteEvent.DeselectAllNotes)
    }

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

    ComposeMainScreenState(
        state = state,
        currentMainScreenState = currentMainScreenState,
        onComposed = onComposed,
        onNotesEvent = onNotesEvent
    )

    if (state.showConfirmDeleteDialog) {
        SimpleDeleteDialog(
            title = stringResource(id = R.string.remove_notes),
            body = stringResource(id = R.string.remove_notes_confirm_body),
            onConfirmClicked = {
                onNotesEvent(NotesOfBlockNoteEvent.OnRemoveSelectedNotesConfirmed)
            },
            onDismiss = {
                onNotesEvent(NotesOfBlockNoteEvent.OnRemoveSelectedNotesCanceled)
            }
        )
    }

    if (state.showMoveNotesDialog) {
        MoveNotesDialog(
            onDismissDialog = {
                onNotesEvent(
                    NotesOfBlockNoteEvent.OnMoveNotesCanceled
                )
            },
            onConfirmClicked = {
                onNotesEvent(
                    NotesOfBlockNoteEvent.OnMoveNotesConfirmed
                )
            },
            expanded = state.moveNotesBlockNotesExpanded,
            onExpandedChanged = {
                onNotesEvent(
                    NotesOfBlockNoteEvent.ExpandMoveNotesBlockNotesList(it)
                )
            },
            selectedBlockNoteId = state.selectedBlockNoteToMoveNotes,
            availableBlockNotes = state.availableBlockNotes,
            onDismissBlockNotesList = {
                onNotesEvent(
                    NotesOfBlockNoteEvent.ExpandMoveNotesBlockNotesList(false)
                )
            },
            onBlockNoteSelected = {
                onNotesEvent(
                    NotesOfBlockNoteEvent.OnMoveNotesNewBlockNoteSelected(it)
                )
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            val currentOrderByStr = when (state.orderBy) {
                OrderBy.Title -> stringResource(id = R.string.title)
                OrderBy.CreatedAt(DateOrderType.OLDER) -> stringResource(id = R.string.creation_date_older)
                OrderBy.CreatedAt(DateOrderType.RECENT) -> stringResource(id = R.string.creation_date_recent)
                OrderBy.UpdatedAt(DateOrderType.OLDER) -> stringResource(id = R.string.updated_date_older)
                OrderBy.UpdatedAt(DateOrderType.RECENT) -> stringResource(id = R.string.updated_date_recent)
                else -> stringResource(id = R.string.creation_date_older)
            }

            ExposedDropdownMenuBox(
                modifier = Modifier.align(Alignment.End),
                expanded = state.isOrderByExpanded,
                onExpandedChange = {
                    onNotesEvent(NotesOfBlockNoteEvent.ExpandOrderByMenuChanged(it))
                }) {
                BasicTextField(
                    value = currentOrderByStr,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    decorationBox = { innerText ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                innerText()
                            }
                            Icon(
                                imageVector = if (!state.isOrderByExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                                contentDescription = null
                            )
                        }
                    },
                    //trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .widthIn(min = 200.dp)
                )
                ExposedDropdownMenu(expanded = state.isOrderByExpanded, onDismissRequest = {
                    onNotesEvent(NotesOfBlockNoteEvent.ExpandOrderByMenuChanged(false))
                }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.title)) },
                        onClick = {
                            onNotesEvent(NotesOfBlockNoteEvent.OnOrderByChanged(OrderBy.Title))
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.creation_date_recent)) },
                        onClick = {
                            onNotesEvent(
                                NotesOfBlockNoteEvent.OnOrderByChanged(
                                    OrderBy.CreatedAt(
                                        DateOrderType.RECENT
                                    )
                                )
                            )
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.creation_date_older)) },
                        onClick = {
                            onNotesEvent(
                                NotesOfBlockNoteEvent.OnOrderByChanged(
                                    OrderBy.CreatedAt(
                                        DateOrderType.OLDER
                                    )
                                )
                            )
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.updated_date_recent)) },
                        onClick = {
                            onNotesEvent(
                                NotesOfBlockNoteEvent.OnOrderByChanged(
                                    OrderBy.UpdatedAt(
                                        DateOrderType.RECENT
                                    )
                                )
                            )
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.updated_date_older)) },
                        onClick = {
                            onNotesEvent(
                                NotesOfBlockNoteEvent.OnOrderByChanged(
                                    OrderBy.UpdatedAt(
                                        DateOrderType.OLDER
                                    )
                                )
                            )
                        })
                }

            }
            NotesList(
                notes = state.notes,
                visualizationType = state.visualizationType,
                onNoteClicked = { id, blockNoteId ->
                    onNotesEvent(NotesOfBlockNoteEvent.OnNoteClicked(id, blockNoteId))
                },
                onNoteLongClicked = {
                    onNotesEvent(
                        NotesOfBlockNoteEvent.MultiSelectionStateChanged(
                            active = true,
                            id = it
                        )
                    )
                },
                isMultiSelectionActive = state.isMultiSelectionActive,
                onSelectionChanged = { id, selected ->
                    onNotesEvent(
                        NotesOfBlockNoteEvent.OnSelectionChanged(
                            id,
                            selected
                        )
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComposeMainScreenState(
    state: RecentNotesState,
    currentMainScreenState: MainScreenState,
    onComposed: (MainScreenState) -> Unit,
    onNotesEvent: (NotesOfBlockNoteEvent) -> Unit,
) {
    val appTitle = stringResource(id = it.thefedex87.core.R.string.app_name)

    LaunchedEffect(
        key1 = state
    ) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appTitle,
                topBarVisible = true,
                bottomBarVisible = true,
                topBarActions = {
                    if (!state.isMultiSelectionActive) {
                        IconButton(onClick = {
                            onNotesEvent(
                                NotesOfBlockNoteEvent.OnAddNewNoteClicked(1)
                            )
                        }, modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(id = R.string.remove_notes)
                            )
                        }
                    } else {
                        IconButton(onClick = {
                            onNotesEvent(
                                NotesOfBlockNoteEvent.OnMoveNotesRequested
                            )
                        }, modifier = Modifier.size(48.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.export_notes),
                                contentDescription = stringResource(id = R.string.move_notes)
                            )
                        }

                        IconButton(onClick = {
                            onNotesEvent(
                                NotesOfBlockNoteEvent.OnRemoveSelectedNotesClicked
                            )
                        }, modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(id = R.string.remove_notes)
                            )
                        }
                    }
                    when (state.visualizationType) {
                        VisualizationType.Grid -> {
                            IconButton(onClick = {
                                onNotesEvent(
                                    NotesOfBlockNoteEvent.OnVisualizationTypeChanged(
                                        VisualizationType.List
                                    )
                                )
                            }, modifier = Modifier.size(48.dp)) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.List,
                                    contentDescription = stringResource(id = it.thefedex87.core.R.string.list_view)
                                )
                            }
                        }

                        VisualizationType.List -> {
                            IconButton(onClick = {
                                onNotesEvent(
                                    NotesOfBlockNoteEvent.OnVisualizationTypeChanged(
                                        VisualizationType.Grid
                                    )
                                )
                            }, modifier = Modifier.size(48.dp)) {
                                Icon(
                                    imageVector = Icons.Default.GridView,
                                    contentDescription = stringResource(id = it.thefedex87.core.R.string.grid_view)
                                )
                            }
                        }
                    }
                }
            )
        )
    }
}