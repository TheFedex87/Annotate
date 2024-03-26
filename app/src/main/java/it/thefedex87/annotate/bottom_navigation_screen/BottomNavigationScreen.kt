package it.thefedex87.annotate.bottom_navigation_screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import it.thefedex87.annotate.R
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.notes_presentation.block_note.BlockNotesEvent
import it.thefedex87.notes_presentation.block_note.BlockNotesView
import it.thefedex87.notes_presentation.block_note.BlockNotesViewModel
import it.thefedex87.notes_presentation.note.screens.add_edit_note.AddEditNoteScreen
import it.thefedex87.notes_presentation.note.screens.add_edit_note.AddEditNoteViewModel
import it.thefedex87.notes_presentation.note.screens.notes_of_block_note.NotesOfBlockNoteEvent
import it.thefedex87.notes_presentation.note.screens.notes_of_block_note.NotesOfBlockNoteScreen
import it.thefedex87.notes_presentation.note.screens.notes_of_block_note.NotesOfBlockNoteViewModel
import it.thefedex87.notes_presentation.note.screens.recent.RecentNotesScreen
import it.thefedex87.notes_presentation.note.screens.recent.RecentNotesViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            AppBar(
                title = mainScreenState.topBarTitle,
                scrollBehavior = scrollBehavior,
                onBackPressed = mainScreenState.topBarBackPressed,
                actions = mainScreenState.topBarActions ?: {}
            )
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
            startDestination = Routes.RECENT_NOTES,
            modifier = modifier.padding(values)
        ) {
            composable(route = Routes.RECENT_NOTES) {
                val viewModel = hiltViewModel<RecentNotesViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                RecentNotesScreen(
                    state = state,
                    onComposed = {
                        mainScreenState = it.copy(
                            topBarBackPressed = null
                        )
                    },
                    uiEvent = viewModel.uiEvent,
                    snackbarHostState = snackbarHostState,
                    currentMainScreenState = mainScreenState,
                    onNotesEvent = {
                        if (it is NotesOfBlockNoteEvent.OnAddNewNoteClicked) {
                            navController.navigate("${Routes.ADD_EDIT_NOTE}/${it.blockNoteId}")
                        } else if(it is NotesOfBlockNoteEvent.OnNoteClicked) {
                            navController.navigate("${Routes.ADD_EDIT_NOTE}/${it.blockNoteId}?noteId=${it.id}")
                        } else {
                            viewModel.onEvent(it)
                        }
                    }
                )

            }
            composable(route = Routes.NOTEBOOKS) {
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
                        mainScreenState = it.copy(
                            topBarBackPressed = null
                        )
                    },
                    currentMainScreenState = mainScreenState,
                    onBlockNotesEvent = {
                        if (it is BlockNotesEvent.OnBlockNoteClicked) {
                            navController.navigate("${Routes.NOTES_OF_BLOCK_NOTE}/${it.blockNote.id}")
                        } else {
                            viewModel.onEvent(it)
                        }
                    },
                    onAddBlockNoteEvent = viewModel::onAddBlockNoteEvent,
                    uiEvent = viewModel.uiEvent,
                    snackbarHostState = snackbarHostState
                )
            }
            composable(
                route = "${Routes.NOTES_OF_BLOCK_NOTE}/{blockNoteId}",
                arguments = listOf(
                    navArgument("blockNoteId") {
                        type = NavType.LongType
                    }
                )
            ) {
                val viewModel = hiltViewModel<NotesOfBlockNoteViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()
                Log.d(Consts.TAG, "Received new state: $state")

                NotesOfBlockNoteScreen(
                    state = state,
                    onComposed = {
                        mainScreenState = it.copy(
                            topBarBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    },
                    uiEvent = viewModel.uiEvent,
                    snackbarHostState = snackbarHostState,
                    currentMainScreenState = mainScreenState,
                    onNotesEvent = {
                        if (it is NotesOfBlockNoteEvent.OnAddNewNoteClicked) {
                            navController.navigate("${Routes.ADD_EDIT_NOTE}/${it.blockNoteId}")
                        } else if(it is NotesOfBlockNoteEvent.OnNoteClicked) {
                            navController.navigate("${Routes.ADD_EDIT_NOTE}/${it.blockNoteId}?noteId=${it.id}")
                        } else {
                            viewModel.onEvent(it)
                        }
                    }
                )
            }
            composable(
                route = "${Routes.ADD_EDIT_NOTE}/{blockNoteId}?noteId={noteId}",
                arguments = listOf(
                    navArgument(name = "blockNoteId") {
                        type = NavType.LongType
                    },
                    navArgument(name = "noteId") {
                        type = NavType.LongType
                        defaultValue = 0
                    }
                )
            ) {
                val viewModel = hiltViewModel<AddEditNoteViewModel>()
                val state by viewModel.state.collectAsStateWithLifecycle()

                AddEditNoteScreen(
                    title = state.title,
                    onAddEditNoteEvent = viewModel::onEvent,
                    snackbarHostState = snackbarHostState,
                    uiEvent = viewModel.uiEvent,
                    note = state.noteState,
                    createdAt = state.createdAt,
                    currentMainScreenState = mainScreenState,
                    onComposed = {
                        mainScreenState = it.copy(
                            topBarBackPressed = {
                                navController.popBackStack()
                            }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackPressed: (() -> Unit)?,
    actions: @Composable RowScope.() -> Unit = { },
) {

    TopAppBar(
        title = {
            Text(text = title)
        },
        actions = actions,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (onBackPressed != null) {
                IconButton(
                    onClick = {
                        onBackPressed.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.navigate_back)
                    )
                }
            }
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