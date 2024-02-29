package it.thefedex87.notes_presentation.block_note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.R
import it.thefedex87.core.R as coreResources

@Composable
fun BlockNoteGridTile(
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

    Box(
        modifier = modifier.padding(spacing.spaceMedium),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onBlockNoteClicked(id)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.book),
                contentDescription = name,
                colorFilter = ColorFilter.tint(
                    color = Color(color)
                ),
                modifier = Modifier
                    .size(140.dp)
                    .padding(spacing.spaceMedium)
            )
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        BlockNoteSettings(
            id = id,
            onBlockNoteOptionsClicked = onBlockNoteOptionsClicked,
            onDismissOptionsRequested = onDismissOptionsRequested,
            onEditBlockNoteClicked = onEditBlockNoteClicked,
            onRemoveBlockNoteClicked = onRemoveBlockNoteClicked,
            showOptions = showOptions,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}