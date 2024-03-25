package it.thefedex87.notes_presentation.block_note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.R

@Composable
fun BlockNoteListTile(
    id: Long,
    name: String,
    color: Int,
    onBlockNoteClicked: (Long) -> Unit,
    onBlockNoteOptionsClicked: (Long) -> Unit,
    onDismissOptionsRequested: () -> Unit,
    onEditBlockNoteClicked:(Long) -> Unit,
    onRemoveBlockNoteClicked: (Long) -> Unit,
    showOptions: Boolean,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier
            .clickable {
                onBlockNoteClicked(id)
            }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.notebook),
            contentDescription = name,
            colorFilter = ColorFilter.tint(
                color = Color(color)
            ),
            modifier = Modifier
                .padding(spacing.spaceMedium)
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        BlockNoteSettings(
            id = id,
            onBlockNoteOptionsClicked = onBlockNoteOptionsClicked,
            onDismissOptionsRequested = onDismissOptionsRequested,
            onEditBlockNoteClicked = onEditBlockNoteClicked,
            onRemoveBlockNoteClicked = onRemoveBlockNoteClicked,
            showOptions = showOptions
        )
    }
}