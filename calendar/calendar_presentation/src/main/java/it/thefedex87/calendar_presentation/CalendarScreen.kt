package it.thefedex87.calendar_presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.thefedex87.core.R
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.theme.LocalSpacing
import kotlinx.coroutines.flow.Flow
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalDate

@Composable
fun CalendarScreen(
    state: CalendarState,
    onComposed: (MainScreenState) -> Unit,
    currentMainScreenState: MainScreenState,
    calendarEvent: (CalendarEvent) -> Unit
) {
    val spacing = LocalSpacing.current

    ComposeMainScreenState(
        state = state,
        currentMainScreenState = currentMainScreenState,
        onComposed = onComposed
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = state.currentMonth.year.toString(),
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                calendarEvent(CalendarEvent.OnPrevMonthClicked)
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Text(text = state.currentMonth.month.name)
            IconButton(onClick = {
                calendarEvent(CalendarEvent.OnNextMonthClicked)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(7)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "M")
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "T")
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "W")
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "T")
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "F")
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "S")
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "S")
                }
            }
            items(state.days) { date ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (date == LocalDate.now()) {
                                Modifier.border(
                                    width = 2.dp,
                                    brush = SolidColor(MaterialTheme.colorScheme.primary),
                                    shape = RectangleShape
                                )
                            } else if (date == state.selectedDay) {
                                Modifier.border(
                                    width = 2.dp,
                                    brush = SolidColor(MaterialTheme.colorScheme.tertiary),
                                    shape = RectangleShape
                                )
                            } else {
                                Modifier
                            }
                        )
                        .clickable {
                            calendarEvent(CalendarEvent.OnSelectedDayChanged(date))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        modifier = Modifier.padding(
                            vertical = spacing.spaceSmall
                        ),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = if (date.month == state.currentMonth.month) MaterialTheme.colorScheme.onBackground
                            else Color(125, 125, 125, 255)
                        )
                    )
                }

            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding()
        ) {
            Text(
                modifier = Modifier
                    .padding(spacing.spaceSmall),
                text = state.selectedDay.toString()
            )
            Text(
                text = "No events for this day",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(spacing.spaceLarge),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 22.sp
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ComposeMainScreenState(
    state: CalendarState,
    currentMainScreenState: MainScreenState,
    onComposed: (MainScreenState) -> Unit,
) {
    val appBarTitle = stringResource(id = R.string.app_name)

    LaunchedEffect(
        key1 = state
    ) {
        onComposed(
            currentMainScreenState.copy(
                topBarTitle = appBarTitle,
                topBarVisible = true,
                bottomBarVisible = true,
                topBarActions = null
            )
        )
    }
}