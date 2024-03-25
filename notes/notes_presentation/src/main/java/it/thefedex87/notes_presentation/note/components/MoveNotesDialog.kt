package it.thefedex87.notes_presentation.note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.R
import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoveNotesDialog(
    onDismissDialog: () -> Unit,
    onConfirmClicked:() -> Unit,
    expanded: Boolean,
    onExpandedChanged: (Boolean) -> Unit,
    selectedBlockNoteId: Long?,
    availableBlockNotes: List<BlockNoteUiModel>,
    onDismissBlockNotesList: () -> Unit,
    onBlockNoteSelected: (Long) -> Unit
) {
    val spacing = LocalSpacing.current
    val selectedBlockNote = availableBlockNotes.first {
        it.id == (selectedBlockNoteId ?: availableBlockNotes.first().id)
    }

    Dialog(onDismissRequest = onDismissDialog) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(12.dp))
                .padding(spacing.spaceMedium)
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.move_notes),
                    style = MaterialTheme.typography.titleLarge
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = onExpandedChanged,
                    modifier = Modifier.padding(spacing.spaceSmall)
                ) {
                    OutlinedTextField(
                        value = selectedBlockNote.name,
                        onValueChange = {},
                        readOnly = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.notebook),
                                contentDescription = null,
                                tint = Color(selectedBlockNote.color)
                            )
                        },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = onDismissBlockNotesList
                    ) {
                        availableBlockNotes.forEach { blockNote ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = blockNote.name)
                                },
                                onClick = {
                                    onBlockNoteSelected(blockNote.id)
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.notebook),
                                        contentDescription = null,
                                        tint = Color(blockNote.color)
                                    )
                                }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissDialog) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    TextButton(
                        onClick = onConfirmClicked
                    ) {
                        Text(text = "Move")
                    }
                }
            }
        }
    }
}