@file:OptIn(ExperimentalFoundationApi::class)

package it.thefedex87.search.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.search.presentation.components.SearchTile

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    modifier: Modifier = Modifier
) {
    ComposeMainScreenState(
        state = state,
        currentMainScreenState = currentMainScreenState,
        onComposed = onComposed
    )

    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium)
    ) {
        var isFocused by remember {
            mutableStateOf(false)
        }
        BasicTextField2(
            state = state.searchText,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    if (isFocused) {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.05f
                        )
                    } else {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.04f
                        )
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isFocused) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.3f
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .fillMaxWidth(),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            decorator = { innerText ->
                if (state.searchText.text.isEmpty() && !isFocused) {
                    Text(
                        stringResource(id = R.string.search_hint),
                        color = Color.Gray
                    )
                } else {
                    innerText()
                }
            }
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                if (state.foundBlockNotes.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.found_block_notes),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(state.foundBlockNotes) {
                SearchTile(
                    onClick = {
                        onEvent(SearchEvent.OnBlockNoteClicked(it.id!!))
                    },
                    title = it.name,
                    icon = painterResource(id = R.drawable.notebook),
                    tint = Color(it.color)
                )
            }
            item {
                if (state.foundNotes.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.found_notes),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(state.foundNotes) {
                SearchTile(
                    onClick = {
                        onEvent(
                            SearchEvent.OnNoteClicked(
                                blockNoteId = it.blockNote.id!!,
                                noteId = it.id!!
                            )
                        )
                    },
                    title = it.title,
                    icon = painterResource(id = R.drawable.baseline_note_24),
                    tint = Color(it.blockNote.color)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SearchScreenPreview() {
    SearchScreen(
        state = SearchState(),
        onEvent = {},
        onComposed = {},
        currentMainScreenState = MainScreenState()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComposeMainScreenState(
    state: SearchState,
    currentMainScreenState: MainScreenState,
    onComposed: (MainScreenState) -> Unit,
) {
    val appBarTitle = stringResource(id = it.thefedex87.core.R.string.app_name)

    LaunchedEffect(
        key1 = state
    ) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appBarTitle,
                topBarVisible = true,
                bottomBarVisible = true,
                topBarActions = null
            )
        )
    }
}