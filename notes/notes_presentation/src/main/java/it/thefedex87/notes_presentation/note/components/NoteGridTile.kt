package it.thefedex87.notes_presentation.note.components

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
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.note.model.NoteUiModel

@Composable
fun NoteGridTile(
    note: NoteUiModel,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.spaceSmall)
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
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}