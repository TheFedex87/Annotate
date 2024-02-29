package it.thefedex87.annotate.bottom_navigation_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import it.thefedex87.core_ui.utils.UiText
import it.thefedex87.annotate.R

sealed class BottomNavScreen(
    val route: String,
    val icon: ImageVector,
    val title: UiText
) {
    data object Notebooks : BottomNavScreen(
        route = Routes.NOTEBOOKS,
        icon = Icons.AutoMirrored.Filled.MenuBook,
        title = UiText.StringResource(
            R.string.notebooks
        )
    )

    data object RecentNotes : BottomNavScreen(
        route = Routes.RECENT_NOTES,
        icon = Icons.Default.Check,
        title = UiText.StringResource(
            R.string.recents
        )
    )
}

data class BottomNavScreenMenuItem(
    val route: String,
    val icon: ImageVector,
    val title: UiText 
)

@Composable
fun prepareBottomNavBarItems(): List<BottomNavScreenMenuItem> {
    return listOf(
        BottomNavScreenMenuItem(
            route = Routes.NOTEBOOKS,
            icon = Icons.AutoMirrored.Filled.MenuBook,
            title = UiText.StringResource(
                R.string.notebooks
            )
        ),
        BottomNavScreenMenuItem(
            route = Routes.RECENT_NOTES,
            icon = ImageVector.vectorResource(R.drawable.clock),
            title = UiText.StringResource(
                R.string.notebooks
            )
        )
    )
}