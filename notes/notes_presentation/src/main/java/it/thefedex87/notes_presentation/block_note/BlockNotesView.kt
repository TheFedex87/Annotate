package it.thefedex87.notes_presentation.block_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AddToHomeScreen
import androidx.compose.material.icons.automirrored.filled.List
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
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.block_note.components.BlockNoteGridTile
import it.thefedex87.notes_presentation.block_note.components.BlockNoteListTile

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BlockNotesView(
    state: BlockNotesState,
    // blockNotes: ImmutableList<BlockNoteDomainModel>,
    // query: String,
    onBlockNotesEvent: (BlockNotesEvent) -> Unit,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val appBarTitle = stringResource(id = R.string.app_name)
    LaunchedEffect(key1 = state.visualizationType) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appBarTitle,
                topBarVisible = true,
                topBarActions = {
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
                    it.id
                }
            ) { blockNote ->
                BlockNoteListTile(
                    id = blockNote.id,
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