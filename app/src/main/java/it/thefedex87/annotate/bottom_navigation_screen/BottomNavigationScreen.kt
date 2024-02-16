package it.thefedex87.annotate.bottom_navigation_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.notes_presentation.block_note.BlockNotesEvent
import it.thefedex87.notes_presentation.block_note.BlockNotesView
import it.thefedex87.notes_presentation.block_note.BlockNotesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var mainScreenState by remember {
        mutableStateOf(
            MainScreenState(
                bottomBarVisible = true
            )
        )
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            AppBar(title = mainScreenState.topBarTitle)
        },
        bottomBar = {
            BottomBar(
                bottomBarVisible = mainScreenState.bottomBarVisible,
                navController = navController
            )
        }
    ) { values ->
        NavHost(
            navController = navController,
            startDestination = BottomNavScreen.Notebooks.route,
            modifier = modifier.padding(values)
        ) {
            composable(route = BottomNavScreen.Notebooks.route) {
                val viewModel = hiltViewModel<BlockNotesViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                /* val blockNotes by viewModel.blockNotes.collectAsStateWithLifecycle()
                val query by viewModel.query.collectAsStateWithLifecycle()
                Log.d(Consts.TAG, "BLock notes list is: ${blockNotes.hashCode()}")
                val blockNoteClickedLambda = remember<(Long) -> Unit> {
                    { id ->
                        viewModel.onEvent(BlockNotesEvent.OnBlockNoteClicked(id))
                    }
                }
                val onQueryChangedLambda = remember<(String) -> Unit> {
                    { query ->
                        viewModel.onEvent(BlockNotesEvent.OnQueryChanged(query))
                    }
                } */

                BlockNotesView(
                    // blockNotes = blockNotes,
                    // query = query,
                    state = state,
                    onComposed = {
                        mainScreenState = it
                    },
                    currentMainScreenState = mainScreenState,
                    onBlockNoteClicked = { id ->
                        viewModel.onEvent(BlockNotesEvent.OnBlockNoteClicked(id))
                    },
                    onQueryChanged = { query ->
                        viewModel.onEvent(BlockNotesEvent.OnQueryChanged(query))
                    }
                )
            }
            composable(route = BottomNavScreen.RecentNotes.route) {
                Text(text = "Recents")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String
) {
    TopAppBar(
        title = {
            Text(text = title)
        }
    )
}

@Composable
fun BottomBar(
    bottomBarVisible: Boolean,
    navController: NavHostController
) {
    val screens = prepareBottomNavBarItems()

    AnimatedVisibility(
        visible = bottomBarVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        NavigationBar(
            //containerColor = Color(0xFF191C1D),
            //tonalElevation = 0.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavScreenMenuItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = screen.title.asString(LocalContext.current)
            )
        },
        label = {
            Text(text = screen.title.asString(LocalContext.current))
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    )
}