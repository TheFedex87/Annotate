package it.thefedex87.notes_presentation.note.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.theme.LocalSpacing
import it.thefedex87.notes_presentation.note.model.NoteUiModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteGridTile(
    note: NoteUiModel,
    onNoteClicked: (Long) -> Unit,
    onNoteLongClicked: (Long) -> Unit,
    isMultiSelectionActive: Boolean,
    selectionChanged: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    Log.d(Consts.TAG, "Note is selected: ${note.isSelected}")

    val scale = animateFloatAsState(
        targetValue = if (note.isSelected) 1.04f else 1f,
        animationSpec = tween(durationMillis = 400),
        label = ""
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(spacing.spaceSmall)
            .scale(scale.value)
            .combinedClickable(
                onClick = {
                    onNoteClicked(note.id)
                },
                onLongClick = {
                    onNoteLongClicked(note.id)
                }
            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(modifier = Modifier.padding(spacing.spaceSmall)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isMultiSelectionActive) {
                    Checkbox(
                        checked = note.isSelected,
                        onCheckedChange = {
                            selectionChanged(note.id, it)
                        }
                    )
                }
                Text(
                    text = note.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceExtraSmall))
            Text(
                text = note.body,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}