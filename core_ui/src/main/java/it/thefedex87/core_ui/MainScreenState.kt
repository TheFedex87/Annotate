package it.thefedex87.core_ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class MainScreenState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val topBarColor: Color? = null,
    val topBarVisible: Boolean = false,
    val bottomBarVisible: Boolean = true,
    val topBarTitle: String = "",
    val topBarActions: (@Composable RowScope.() -> Unit)? = null,
    val topBarShowBack: Boolean = true,
    val topBarBackPressed: (() -> Unit)? = null,
    val topAppBarScrollBehavior: (@Composable () -> TopAppBarScrollBehavior)? = null,
    val fabState: BottomNavigationScreenFabState = BottomNavigationScreenFabState(),
    val prevFabState: BottomNavigationScreenFabState = BottomNavigationScreenFabState()
)

data class BottomNavigationScreenFabState(
    val floatingActionButtonVisible: Boolean = false,
    val floatingActionButtonIcon: ImageVector? = null,
    val floatingActionButtonLabel: String? = null,
    val floatingActionButtonClicked: (() -> Unit)? = null,
    //val floatingActionButtonMultiChoice: List<MiniFabSpec>? = emptyList(),
    val floatingActionButtonMultiChoiceExtended: Boolean = false
)
