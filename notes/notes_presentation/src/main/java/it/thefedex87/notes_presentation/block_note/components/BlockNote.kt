package it.thefedex87.notes_presentation.block_note.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.thefedex87.core_ui.LocalSpacing
import it.thefedex87.notes_presentation.R

@Composable
fun BlockNote(
    id: Long,
    name: String,
    color: Int,
    onBlockNoteClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .clickable {
               onBlockNoteClicked(id)
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
            Text(text = name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}