package it.thefedex87.core_ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.thefedex87.core_ui.R
import it.thefedex87.core_ui.theme.LocalSpacing

@Composable
fun SimpleDeleteDialog(
    title: String,
    body: String,
    onConfirmClicked: () -> Unit,
    onDismiss: () -> Unit,
    cancelButtonText: String = stringResource(id = R.string.cancel),
    confirmButtonText: String = stringResource(id = R.string.confirm)
) {
    val spacing = LocalSpacing.current
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(12.dp))
                .padding(spacing.spaceMedium)
        ) {
            Column {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    Text(
                        text = body,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = cancelButtonText)
                        }
                        Spacer(modifier = Modifier.width(spacing.spaceSmall))
                        TextButton(
                            onClick = onConfirmClicked
                        ) {
                            Text(text = confirmButtonText)
                        }
                    }
                }
            }
        }
    }
}