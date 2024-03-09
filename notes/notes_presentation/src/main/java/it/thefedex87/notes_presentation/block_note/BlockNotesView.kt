package it.thefedex87.notes_presentation.block_note

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.thefedex87.core.R
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.components.SafeDeleteDialog
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.block_note.addEditBlockNote.AddEditBlockNote
import it.thefedex87.notes_presentation.block_note.addEditBlockNote.AddEditBlockNoteEvent
import it.thefedex87.notes_presentation.block_note.components.BlockNoteGridTile
import it.thefedex87.notes_presentation.block_note.components.BlockNoteListTile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import it.thefedex87.notes_presentation.R as NotesPresentationR

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BlockNotesView(
    state: BlockNotesState,
    // blockNotes: ImmutableList<BlockNoteDomainModel>,
    // query: String,
    uiEvent: Flow<UiEvent>,
    onBlockNotesEvent: (BlockNotesEvent) -> Unit,
    onAddBlockNoteEvent: (AddEditBlockNoteEvent) -> Unit,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    ComposeMainScreenState(
        state = state,
        currentMainScreenState = currentMainScreenState,
        onComposed = onComposed,
        onBlockNotesEvent = onBlockNotesEvent
    )

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

    if (state.addEditBlockNoteState.showDialog) {
        AddEditBlockNote(
            state = state.addEditBlockNoteState,
            onAddBlockNoteEvent = onAddBlockNoteEvent
        )
    } else if (state.deleteBlockNoteState.showDialog) {
        SafeDeleteDialog(
            title = stringResource(id = R.string.delete),
            body = String.format(
                stringResource(id = NotesPresentationR.string.remove_block_note_body_dialog),
                state.deleteBlockNoteState.deleteBlockNoteName
            ),
            validDeletionName = state.deleteBlockNoteState.deleteBlockNoteName,
            onConfirmClicked = {
                onBlockNotesEvent(BlockNotesEvent.OnDeleteBlockNoteConfirmed)
            },
            onCancelClicked = {
                onBlockNotesEvent(BlockNotesEvent.OnDeleteBlockNoteDismissed)
            },
            confirmNamePlaceholder = stringResource(id = NotesPresentationR.string.insert_block_note_name)
        )
    }

    if (state.visualizationType == VisualizationType.Grid) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = modifier.fillMaxSize(),
        ) {
            items(
                state.blockNotes,
                key = {
                    it.id
                }
            ) { blockNote ->
                BlockNoteGridTile(
                    id = blockNote.id,
                    name = blockNote.name,
                    color = blockNote.color,
                    onBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnBlockNoteClicked(blockNote))
                    },
                    onBlockNoteOptionsClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnShowBlockNoteOptionsClicked(it))
                    },
                    onDismissOptionsRequested = {
                        onBlockNotesEvent(BlockNotesEvent.OnDismissBlockNoteOptions)
                    },
                    onEditBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnEditBlockNoteClicked(it))
                    },
                    onRemoveBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnDeleteBlockNoteClicked(it))
                    },
                    showOptions = blockNote.showOptions
                )
            }
        }
    } else if (state.visualizationType == VisualizationType.List) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
        ) {
            items(
                state.blockNotes,
                key = {
                    it.id
                }
            ) { blockNote ->
                BlockNoteListTile(
                    id = blockNote.id,
                    name = blockNote.name,
                    color = blockNote.color,
                    onBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnBlockNoteClicked(blockNote))
                    },
                    onBlockNoteOptionsClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnShowBlockNoteOptionsClicked(it))
                    },
                    onDismissOptionsRequested = {
                        onBlockNotesEvent(BlockNotesEvent.OnDismissBlockNoteOptions)
                    },
                    onEditBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnEditBlockNoteClicked(it))
                    },
                    onRemoveBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnDeleteBlockNoteClicked(it))
                    },
                    showOptions = blockNote.showOptions
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComposeMainScreenState(
    state: BlockNotesState,
    currentMainScreenState: MainScreenState,
    onComposed: (MainScreenState) -> Unit,
    onBlockNotesEvent: (BlockNotesEvent) -> Unit,
) {
    val appBarTitle = stringResource(id = R.string.app_name)

    LaunchedEffect(key1 = state.visualizationType) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appBarTitle,
                topBarVisible = true,
                topBarActions = {
                    IconButton(onClick = {
                        onBlockNotesEvent(
                            BlockNotesEvent.OnAddNewBlockNoteClicked
                        )
                    }, modifier = Modifier.size(48.dp)) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = NotesPresentationR.string.add_blocknote)
                        )
                    }
                    when (state.visualizationType) {
                        VisualizationType.Grid -> {
                            IconButton(onClick = {
                                onBlockNotesEvent(
                                    BlockNotesEvent.OnVisualizationTypeChanged(
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
                                onBlockNotesEvent(
                                    BlockNotesEvent.OnVisualizationTypeChanged(
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