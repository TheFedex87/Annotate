package it.thefedex87.annotate.bottom_navigation_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import it.thefedex87.annotate.R
import it.thefedex87.core_ui.utils.UiText

data class BottomNavScreenMenuItem(
    val route: String,
    val icon: ImageVector,
    val title: UiText 
)

@Composable
fun prepareBottomNavBarItems(): List<BottomNavScreenMenuItem> {
    return listOf(
        BottomNavScreenMenuItem(
            route = Routes.RECENT_NOTES,
            icon = ImageVector.vectorResource(R.drawable.clock),
            title = UiText.StringResource(
                R.string.recent
            )
        ),
        BottomNavScreenMenuItem(
            route = Routes.NOTEBOOKS,
            icon = Icons.AutoMirrored.Filled.MenuBook,
            title = UiText.StringResource(
                R.string.notebooks
            )
        ),
        BottomNavScreenMenuItem(
            route = Routes.CALENDAR,
            icon = Icons.Default.CalendarToday,
            title = UiText.StringResource(
                R.string.calendar
            )
        )
    )
}