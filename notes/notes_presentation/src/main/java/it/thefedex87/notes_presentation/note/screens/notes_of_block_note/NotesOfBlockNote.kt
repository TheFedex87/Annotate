package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.thefedex87.core.R
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.notes_presentation.block_note.BlockNotesEvent
import it.thefedex87.notes_presentation.block_note.BlockNotesState
import it.thefedex87.notes_presentation.note.components.NotesList

@Composable
fun NotesOfBlockNote(
    state: NotesOfBlockNoteState,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    onNotesEvent: (NotesOfBlockNoteEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    ComposeMainScreenState(
        state = state,
        currentMainScreenState = currentMainScreenState,
        onComposed = onComposed,
        onNotesEvent = onNotesEvent
    )

    Box(modifier = modifier.fillMaxSize()) {
        NotesList(notes = state.notes, visualizationType = state.visualizationType)
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
    val appBarTitle =
        state.blockNoteName.ifBlank { stringResource(id = R.string.app_name) }

    LaunchedEffect(key1 = state.visualizationType, key2 = appBarTitle) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appBarTitle,
                topBarVisible = true,
                topBarActions = {
                    IconButton(onClick = {
                        onNotesEvent(
                            NotesOfBlockNoteEvent.OnAddNewNoteClicked
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