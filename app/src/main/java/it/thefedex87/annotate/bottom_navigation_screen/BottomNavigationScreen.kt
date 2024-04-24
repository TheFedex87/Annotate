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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
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
import it.thefedex87.alarms_presentation.AlarmView
import it.thefedex87.alarms_presentation.AlarmViewModel
import it.thefedex87.annotate.R
import it.thefedex87.annotate.ui.theme.AnnotateTheme
import it.thefedex87.calendar_presentation.CalendarScreen
import it.thefedex87.calendar_presentation.CalendarViewModel
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.MainScreenState
import it.thefedex87.notes_presentation.block_note.BlockNotesEvent
import it.thefedex87.notes_presentation.block_note.BlockNotesScreen
import it.thefedex87.notes_presentation.block_note.BlockNotesViewModel
import it.thefedex87.notes_presentation.note.screens.add_edit_note.AddEditNoteScreen
import it.thefedex87.notes_presentation.note.screens.add_edit_note.AddEditNoteViewModel
import it.thefedex87.notes_presentation.note.screens.NotesEvent
import it.thefedex87.notes_presentation.note.screens.NotesScreen
import it.thefedex87.notes_presentation.note.screens.NotesViewModel
import it.thefedex87.notes_presentation.note.screens.add_edit_note.AddEditNoteEvent


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
        if (!LocalInspectionMode.current) {
            NavHost(
                navController = navController,
                startDestination = Routes.RECENT_NOTES,
                modifier = modifier.padding(values)
            ) {
                composable(route = Routes.RECENT_NOTES) {
                    val viewModel = hiltViewModel<NotesViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    Log.d(Consts.TAG, "Received new state: $state")

                    NotesScreen(
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
                            when (it) {
                                is NotesEvent.OnAddNewNoteClicked -> {
                                    navController.navigate("${Routes.ADD_EDIT_NOTE}/1")
                                }

                                is NotesEvent.OnNoteClicked -> {
                                    navController.navigate("${Routes.ADD_EDIT_NOTE}/${it.blockNoteId}?noteId=${it.id}")
                                }

                                else -> {
                                    viewModel.onEvent(it)
                                }
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

                    BlockNotesScreen(
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
                    route = Routes.CALENDAR
                ) {
                    val viewModel = hiltViewModel<CalendarViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    CalendarScreen(
                        state = state,
                        calendarEvent = viewModel::onEvent,
                        onComposed = {
                            mainScreenState = it.copy(
                                topBarBackPressed = null
                            )
                        },
                        currentMainScreenState = mainScreenState
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
                    val viewModel = hiltViewModel<NotesViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    Log.d(Consts.TAG, "Received new state: $state")

                    NotesScreen(
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
                            if (it is NotesEvent.OnAddNewNoteClicked) {
                                navController.navigate("${Routes.ADD_EDIT_NOTE}/${it.blockNoteId}")
                            } else if (it is NotesEvent.OnNoteClicked) {
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

                    val noteId = it.arguments?.getLong("noteId")

                    AddEditNoteScreen(
                        state = state,
                        noteId = noteId,
                        onAddEditNoteEvent = { event ->
                            when (event) {
                                is AddEditNoteEvent.OnSetAlarmClicked -> {
                                    //navController.navigate("${Routes.SET_ALARM}/$noteId")
                                    viewModel.onEvent(event)
                                }

                                else -> viewModel.onEvent(event)
                            }
                        },
                        parentBlockNoteName = state.blockNoteName,
                        snackbarHostState = snackbarHostState,
                        uiEvent = viewModel.uiEvent,
                        createdAt = state.createdAt,
                        currentMainScreenState = mainScreenState,
                        navHostController = navController,
                        onComposed = {
                            mainScreenState = it.copy()
                        }
                    )
                }
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
            Text(
                text = screen.title.asString(LocalContext.current),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = if(LocalDensity.current.fontScale >= 1.5) 12.sp else 16.sp
                ))
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

@Preview
@PreviewScreenSizes
@Composable
fun BottomNavigationScreenPreview() {
    AnnotateTheme {
        BottomNavigationScreen(navController = NavHostController(LocalContext.current))
    }
}