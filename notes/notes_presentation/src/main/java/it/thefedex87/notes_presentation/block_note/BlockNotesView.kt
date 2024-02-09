package it.thefedex87.notes_presentation.block_note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.notes_presentation.block_note.components.BlockNote

@Composable
fun BlockNotesView(
    blockNotes: List<BlockNoteDomainModel>,
    onBlockNoteClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp)) {
            items(blockNotes) { blockNote ->
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