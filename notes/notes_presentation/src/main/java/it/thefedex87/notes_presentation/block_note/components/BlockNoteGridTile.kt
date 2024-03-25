package it.thefedex87.notes_presentation.block_note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.R

@Composable
fun BlockNoteGridTile(
    id: Long,
    name: String,
    color: Int,
    onBlockNoteClicked: (Long) -> Unit,
    onBlockNoteOptionsClicked: (Long) -> Unit,
    onDismissOptionsRequested: () -> Unit,
    onEditBlockNoteClicked: (Long) -> Unit,
    onRemoveBlockNoteClicked: (Long) -> Unit,
    showOptions: Boolean,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .padding(spacing.spaceMedium),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier
                .width(140.dp)
                .clickable {
                    onBlockNoteClicked(id)
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.notebook),
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
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(spacing.spaceSmall)
                )
            }
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

@Composable
@Preview
fun PreviewBlockNoteGridTile() {
    BlockNoteGridTile(
        id = 1,
        name = "Block Note 1",
        color = Color.Magenta.toArgb(),
        onBlockNoteClicked = {},
        onBlockNoteOptionsClicked = {},
        onDismissOptionsRequested = {},
        onEditBlockNoteClicked = {},
        onRemoveBlockNoteClicked = {},
        showOptions = false
    )
}