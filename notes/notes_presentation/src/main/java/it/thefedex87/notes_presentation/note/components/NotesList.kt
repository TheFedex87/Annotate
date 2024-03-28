package it.thefedex87.notes_presentation.note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.R
import it.thefedex87.notes_presentation.note.model.NoteUiModel

@Composable
fun NotesList(
    notes: List<NoteUiModel>,
    parentBlockNote: BlockNoteDomainModel?,
    visualizationType: VisualizationType,
    onNoteClicked: (Long, Long) -> Unit,
    onNoteLongClicked: (Long) -> Unit,
    onSelectionChanged: (Long, Boolean) -> Unit,
    isMultiSelectionActive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.notebook),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.7f),
            colorFilter = ColorFilter.tint(
                color = Color(parentBlockNote?.color ?: MaterialTheme.colorScheme.onBackground.toArgb())
            ),
            alpha = 0.05f
        )
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
                        onNoteClicked = onNoteClicked,
                        onNoteLongClicked = onNoteLongClicked,
                        isMultiSelectionActive = isMultiSelectionActive,
                        selectionChanged = onSelectionChanged
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
                        onNoteClicked = onNoteClicked,
                        onNoteLongClicked = onNoteLongClicked,
                        isMultiSelectionActive = isMultiSelectionActive,
                        selectionChanged = onSelectionChanged
                    )
                }
            }
        }
    }
}