package it.thefedex87.alarms_presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import it.thefedex87.core_ui.theme.LocalSpacing
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmView(
    onCheckedChanged: (Boolean) -> Unit,
    enabled: Boolean,
    onTimeClicked: () -> Unit,
    onDateClicked: () -> Unit,
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int

) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.padding(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.alarm),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = enabled,
                onCheckedChange = {
                    onCheckedChanged(it)
                }
            )
            Box {
                Row {
                    Text(
                        text = "$year/$month/$day",
                        modifier = Modifier.clickable {
                            onDateClicked()
                        }
                    )
                    Spacer(modifier = Modifier.padding(spacing.spaceSmall))
                    Text(
                        text = "${String.format("%02d", hour)}:${String.format("%02d", minute)}",
                        modifier = Modifier.clickable {
                            onTimeClicked()
                        }
                    )
                }
            }
        }
    }
}