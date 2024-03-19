package it.thefedex87.notes_presentation.note.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.note.model.NoteUiModel

@Composable
fun NotesList(
    notes: List<NoteUiModel>,
    visualizationType: VisualizationType,
    onNoteClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (visualizationType == VisualizationType.Grid) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
            modifier = modifier.fillMaxSize()
        ) {
            items(notes,
                key = {
                    it.id
                }
            ) {
                NoteGridTile(
                    note = it,
                    onNoteClicked = onNoteClicked
                )
            }
        }
    } else {
        LazyColumn {
            items(notes,
                key = {
                    it.id
                }
            ) {
                NoteListTile(
                    note = it,
                    onNoteClicked
                )
            }
        }
    }
}