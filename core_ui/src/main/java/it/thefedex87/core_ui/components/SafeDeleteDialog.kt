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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.thefedex87.core_ui.theme.LocalSpacing

@Composable
fun SafeDeleteDialog(
    title: String,
    body: String,
    validDeletionName: String?,
    onConfirmClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    confirmNamePlaceholder: String,
    confirmButtonText: String = "Confirm",
    cancelButtonText: String = "Cancel"
) {
    val spacing = LocalSpacing.current
    var isFocused by remember {
        mutableStateOf(false)
    }
    var currentDeletionName by remember {
        mutableStateOf("")
    }

    Dialog(onDismissRequest = onCancelClicked) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(12.dp))
                .padding(spacing.spaceMedium)
        ) {
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
                if (validDeletionName != null) {
                    BasicTextField(
                        value = currentDeletionName,
                        onValueChange = {
                            currentDeletionName = it
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
                                    if (currentDeletionName.isEmpty()) {
                                        Text(
                                            text = confirmNamePlaceholder,
                                            color = MaterialTheme.colorScheme.outline,
                                            style = MaterialTheme.typography.bodyLarge,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
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
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancelClicked) {
                        Text(text = cancelButtonText)
                    }
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    TextButton(
                        enabled = validDeletionName == null || (validDeletionName == currentDeletionName),
                        onClick = onConfirmClicked
                    ) {
                        Text(text = confirmButtonText)
                    }
                }
            }
        }
    }
}