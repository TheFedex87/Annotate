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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.thefedex87.core.R
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.core_ui.theme.LocalSpacing
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.util.Locale

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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                calendarEvent(CalendarEvent.OnPrevClicked)
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            when (state.currentView) {
                ViewType.MONTH -> {
                    Text(
                        text = "${state.currentMonth!!.month.name} ${state.currentYear}",
                        modifier = Modifier.clickable {
                            calendarEvent(CalendarEvent.OnYearClicked)
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 20.sp
                        )
                    )
                }

                ViewType.YEAR -> {
                    Text(
                        text = "${state.currentYear}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 20.sp
                        )
                    )
                }
            }

            IconButton(onClick = {
                calendarEvent(CalendarEvent.OnNextClicked)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (state.currentView == ViewType.MONTH) 7 else 4)
        ) {
            when (state.currentView) {
                ViewType.MONTH -> {
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
                                    when (date) {
                                        LocalDate.now() -> {
                                            Modifier.border(
                                                width = 2.dp,
                                                brush = SolidColor(MaterialTheme.colorScheme.primary),
                                                shape = RectangleShape
                                            )
                                        }

                                        state.selectedDay -> {
                                            Modifier.border(
                                                width = 2.dp,
                                                brush = SolidColor(MaterialTheme.colorScheme.tertiary),
                                                shape = RectangleShape
                                            )
                                        }

                                        else -> {
                                            Modifier
                                        }
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
                                    color = if (date.month == state.currentMonth!!.month) MaterialTheme.colorScheme.onBackground
                                    else Color(125, 125, 125, 255)
                                )
                            )
                        }
                    }
                }

                ViewType.YEAR -> {
                    items(Month.entries.toTypedArray()) { month ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    calendarEvent(
                                        CalendarEvent
                                            .OnMonthClicked(
                                                YearMonth.of(
                                                    state.currentYear,
                                                    month
                                                )
                                            )
                                    )
                                }
                                .then(
                                    if (month == YearMonth.now().month && state.currentYear == YearMonth.now().year) {
                                        Modifier.border(
                                            width = 2.dp,
                                            brush = SolidColor(MaterialTheme.colorScheme.primary),
                                            shape = RectangleShape
                                        )
                                    } else {
                                        Modifier
                                    }
                                ),
                            contentAlignment = Alignment.Center,

                            ) {
                            Text(
                                text = month.name.uppercase().substring(0, 3),
                                modifier = Modifier.padding(spacing.spaceMedium)
                            )
                        }
                    }
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