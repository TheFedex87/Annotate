package it.thefedex87.annotate.bottom_navigation_screen

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.core.content.ContextCompat
import it.thefedex.core_ui.utils.UiText
import it.thefedex87.annotate.R

sealed class BottomNavScreen(
    val route: String,
    val icon: ImageVector,
    val title: UiText
) {
    data object Notebooks : BottomNavScreen(
        route = Routes.NOTEBOOKS,
        icon = Icons.Default.MenuBook,
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