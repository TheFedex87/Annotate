package it.thefedex87.notes_presentation.note.components

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel
import it.thefedex87.notes_presentation.note.model.NoteUiModel
import java.time.LocalDateTime

@Composable
fun NoteListTile(
    note: NoteUiModel,
    onNoteClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.spaceSmall)
            .clickable {
                onNoteClicked(note.id)
            }
    ) {
        Column(modifier = Modifier.padding(spacing.spaceSmall)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
            Text(
                text = note.body,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
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
            color = Color.RED,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    ),
        onNoteClicked = {})
}