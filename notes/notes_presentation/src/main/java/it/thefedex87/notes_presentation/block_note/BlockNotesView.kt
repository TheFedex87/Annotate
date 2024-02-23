package it.thefedex87.notes_presentation.block_note

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import it.thefedex87.core.R
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.block_note.addBlockNote.AddBlockNote
import it.thefedex87.notes_presentation.block_note.addBlockNote.AddBlockNoteEvent
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
    onAddBlockNoteEvent: (AddBlockNoteEvent) -> Unit,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState? = null
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    composeMainScreenState(
        state = state,
        currentMainScreenState = currentMainScreenState,
        onComposed = onComposed,
        onBlockNotesEvent = onBlockNotesEvent
    )

    LaunchedEffect(key1 = true) {
        uiEvent.onEach {
            when(it) {
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

    if (state.addBlockNoteState.showDialog) {
        AddBlockNote(
            state = state.addBlockNoteState,
            onAddBlockNoteEvent = onAddBlockNoteEvent
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
                    it.id!!
                }
            ) { blockNote ->
                BlockNoteGridTile(
                    id = blockNote.id!!,
                    name = blockNote.name,
                    color = blockNote.color,
                    onBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnBlockNoteClicked(it))
                    }
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
                    it.id!!
                }
            ) { blockNote ->
                BlockNoteListTile(
                    id = blockNote.id!!,
                    name = blockNote.name,
                    color = blockNote.color,
                    onBlockNoteClicked = {
                        onBlockNotesEvent(BlockNotesEvent.OnBlockNoteClicked(it))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun composeMainScreenState(
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
                                    imageVector =  Icons.Default.GridView,
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