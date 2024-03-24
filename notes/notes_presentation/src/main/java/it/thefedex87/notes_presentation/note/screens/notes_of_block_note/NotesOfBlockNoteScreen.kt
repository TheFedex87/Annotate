package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.thefedex87.core.R
import it.thefedex87.core.domain.model.DateOrderType
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.notes_presentation.note.components.NotesList
import it.thefedex87.notes_presentation.R as NotesResources

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesOfBlockNoteScreen(
    state: NotesOfBlockNoteState,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    onNotesEvent: (NotesOfBlockNoteEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(enabled = state.isMultiSelectionActive) {
        onNotesEvent(NotesOfBlockNoteEvent.DeselectAllNotes)
    }

    ComposeMainScreenState(
        state = state,
        currentMainScreenState = currentMainScreenState,
        onComposed = onComposed,
        onNotesEvent = onNotesEvent
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column {
            val currentOrderByStr = when (state.orderBy) {
                OrderBy.Title -> stringResource(id = NotesResources.string.title)
                OrderBy.CreatedAt(DateOrderType.OLDER) -> stringResource(id = NotesResources.string.creation_date_older)
                OrderBy.CreatedAt(DateOrderType.RECENT) -> stringResource(id = NotesResources.string.creation_date_recent)
                OrderBy.UpdatedAt(DateOrderType.OLDER) -> stringResource(id = NotesResources.string.updated_date_older)
                OrderBy.UpdatedAt(DateOrderType.RECENT) -> stringResource(id = NotesResources.string.updated_date_recent)
                else -> stringResource(id = NotesResources.string.creation_date_older)
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
                        text = { Text(text = stringResource(id = NotesResources.string.title)) },
                        onClick = {
                            onNotesEvent(NotesOfBlockNoteEvent.OnOrderByChanged(OrderBy.Title))
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = NotesResources.string.creation_date_recent)) },
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
                        text = { Text(text = stringResource(id = NotesResources.string.creation_date_older)) },
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
                        text = { Text(text = stringResource(id = NotesResources.string.updated_date_recent)) },
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
                        text = { Text(text = stringResource(id = NotesResources.string.updated_date_older)) },
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
                onNoteClicked = {
                    onNotesEvent(NotesOfBlockNoteEvent.OnNoteClicked(it))
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
    state: NotesOfBlockNoteState,
    currentMainScreenState: MainScreenState,
    onComposed: (MainScreenState) -> Unit,
    onNotesEvent: (NotesOfBlockNoteEvent) -> Unit,
) {
    val appBarTitle = if (state.blockNote == null)
        stringResource(id = R.string.app_name)
    else
        state.blockNote.name

    LaunchedEffect(key1 = state.visualizationType, key2 = appBarTitle, key3 = state.blockNote) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appBarTitle,
                topBarVisible = true,
                bottomBarVisible = true,
                topBarActions = {
                    IconButton(onClick = {
                        onNotesEvent(
                            NotesOfBlockNoteEvent.OnAddNewNoteClicked(state.blockNote?.id ?: 0)
                        )
                    }, modifier = Modifier.size(48.dp)) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = it.thefedex87.notes_presentation.R.string.add_blocknote)
                        )
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
                                    contentDescription = stringResource(id = R.string.list_view)
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
                                    contentDescription = stringResource(id = R.string.grid_view)
                                )
                            }
                        }
                    }
                }
            )
        )
    }
}