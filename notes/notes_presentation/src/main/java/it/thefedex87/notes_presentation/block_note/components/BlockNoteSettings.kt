package it.thefedex87.notes_presentation.block_note.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.thefedex87.notes_presentation.R

@Composable
fun BlockNoteSettings(
    id: Long,
    onBlockNoteOptionsClicked: (Long) -> Unit,
    onDismissOptionsRequested: () -> Unit,
    onEditBlockNoteClicked:(Long) -> Unit,
    onRemoveBlockNoteClicked: (Long) -> Unit,
    showOptions: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        IconButton(onClick = {
            onBlockNoteOptionsClicked(id)
        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.block_note_options)
            )
        }
        DropdownMenu(
            expanded = showOptions,
            onDismissRequest = onDismissOptionsRequested
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = it.thefedex87.core.R.string.edit)) },
                onClick = {
                    onEditBlockNoteClicked(id)
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(id = it.thefedex87.core.R.string.remove)) },
                onClick = {
                    onRemoveBlockNoteClicked(id)
                }
            )
        }
    }
}