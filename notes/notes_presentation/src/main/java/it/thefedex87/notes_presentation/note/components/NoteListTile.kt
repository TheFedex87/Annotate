package it.thefedex87.notes_presentation.note.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.R
import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel
import it.thefedex87.notes_presentation.note.model.NoteUiModel
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListTile(
    note: NoteUiModel,
    onNoteClicked: (Long, Long) -> Unit,
    onNoteLongClicked: (Long) -> Unit,
    isMultiSelectionActive: Boolean,
    selectionChanged: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.spaceSmall)
            .combinedClickable(
                onClick = {
                    onNoteClicked(note.id, note.blockNoteUiModel.id)
                },
                onLongClick = {
                    onNoteLongClicked(note.id)
                }
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isMultiSelectionActive) {
                Checkbox(
                    checked = note.isSelected,
                    onCheckedChange = {
                        selectionChanged(note.id, it)
                    }
                )
            }
            Column(modifier = Modifier.padding(spacing.spaceSmall)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Log.d(Consts.TAG, "Color is: ${note.blockNoteUiModel.color}")
                    Icon(
                        painter = painterResource(id = R.drawable.notebook),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(note.blockNoteUiModel.color)
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
                Text(
                    text = note.body,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview
fun NoteListTilePreview() {
    NoteListTile(note = NoteUiModel(
        id = 1,
        title = "Title",
        body = "This is the body of the note and can be ery very very long",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
        blockNoteUiModel = BlockNoteUiModel(
            id = 1,
            name = "Default",
            color = Color.Green.toArgb(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    ),
        onNoteClicked = {id, blockNoteId -> },
        onNoteLongClicked = {},
        isMultiSelectionActive = true,
        selectionChanged = {l, b -> })
}