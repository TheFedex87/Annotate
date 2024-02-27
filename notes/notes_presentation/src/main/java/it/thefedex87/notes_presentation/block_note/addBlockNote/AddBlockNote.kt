package it.thefedex87.notes_presentation.block_note.addBlockNote

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.thefedex87.core.R
import it.thefedex87.core_ui.components.ColorPicker
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.R as NotesPresentationR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddBlockNote(
    state: AddBlockNoteState,
    onAddBlockNoteEvent: (AddBlockNoteEvent) -> Unit
) {
    val spacing = LocalSpacing.current

    var isFocused by remember {
        mutableStateOf(false)
    }

    Dialog(onDismissRequest = {
        onAddBlockNoteEvent(AddBlockNoteEvent.OnDismiss)
    }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(12.dp))
                .padding(spacing.spaceMedium)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = NotesPresentationR.string.create_new_block_note_title),
                    style = MaterialTheme.typography.titleLarge
                )
                BasicTextField(
                    value = state.name,
                    onValueChange = {
                        onAddBlockNoteEvent(AddBlockNoteEvent.OnNameChanged(it))
                    },
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    decorationBox = { innerText ->
                        Column {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                innerText()
                                if (state.name.isEmpty()) {
                                    Text(
                                        text = stringResource(id = NotesPresentationR.string.insert_name_here),
                                        color = MaterialTheme.colorScheme.outline,
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                }
                            }
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = spacing.spaceExtraSmall),
                                thickness = 1.dp,
                                color = if (isFocused) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(
                            horizontal = spacing.spaceSmall,
                            vertical = spacing.spaceMedium
                        )
                        .onFocusChanged {
                            isFocused = it.hasFocus
                        }
                )
                ColorPicker(
                    onSelectNewColor = {
                        onAddBlockNoteEvent(AddBlockNoteEvent.OnSelectedNewColor(it))
                    },
                    selectedColor = Color(state.selectedColor)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        onAddBlockNoteEvent(AddBlockNoteEvent.OnDismiss)
                    }) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    TextButton(
                        enabled = state.name.isNotBlank() && state.name.isNotEmpty(),
                        onClick = {
                            onAddBlockNoteEvent(AddBlockNoteEvent.OnConfirmClicked)
                        }) {
                        Text(text = stringResource(id = if(state.id == null) R.string.create else R.string.edit))
                    }
                }
            }
        }
    }
}