package it.thefedex87.notes_presentation.block_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import it.thefedex87.core.R
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.components.OutlinedTextFieldWithErrorMessage
import it.thefedex87.notes_presentation.block_note.components.BlockNote

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BlockNotesView(
    state: BlockNotesState,
    // blockNotes: ImmutableList<BlockNoteDomainModel>,
    // query: String,
    onBlockNoteClicked: (Long) -> Unit,
    onQueryChanged: (String) -> Unit,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    modifier: Modifier = Modifier
) {
    val appBarTitle = stringResource(id = R.string.app_name)
    LaunchedEffect(key1 = true) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appBarTitle,
                topBarVisible = true,
                topBarActions = {

                }
            )
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextFieldWithErrorMessage(
            value = state.userInputTest,
            onValueChanged = onQueryChanged,
            imeAction = ImeAction.Search,
            label = "Query",
            modifier = Modifier.padding(4.dp)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 150.dp),
            modifier = Modifier.weight(1f),
        ) {
            items(
                state.blockNotes,
                key = {
                    it.id
                }
            ) { blockNote ->
                BlockNote(
                    id = blockNote.id,
                    name = blockNote.name,
                    color = blockNote.color,
                    onBlockNoteClicked = onBlockNoteClicked
                )
            }
        }
    }
}