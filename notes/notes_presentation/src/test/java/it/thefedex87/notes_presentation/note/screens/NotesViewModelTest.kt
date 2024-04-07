package it.thefedex87.notes_presentation.note.screens

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.DateOrderType
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.NotesRepositoryFake
import it.thefedex87.notes_presentation.block_note.utils.MainCoroutineExtension
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
internal class NotesViewModelTest {
    private lateinit var viewModel: NotesViewModel
    private lateinit var notesRepositoryFake: NotesRepositoryFake
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var collectJob: Job

    @BeforeEach
    fun setUp() {
        notesRepositoryFake = NotesRepositoryFake()
        savedStateHandle = SavedStateHandle()

        viewModel = NotesViewModel(
            notesRepositoryFake,
            savedStateHandle
        )

        collectJob = CoroutineScope(Dispatchers.Main).launch {
            viewModel.state.collect()
        }
    }

    @AfterEach
    fun tearDown() {
        collectJob.cancel()
    }

    @Test
    fun `verify updating block note visualization type the state is correctly updated`() = runTest {
        viewModel.onEvent(NotesEvent.OnVisualizationTypeChanged(VisualizationType.List))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.visualizationType).isEqualTo(VisualizationType.List)

        viewModel.onEvent(NotesEvent.OnVisualizationTypeChanged(VisualizationType.Grid))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.visualizationType).isEqualTo(VisualizationType.Grid)
    }

    @RepeatedTest(value = 100)
    fun `verify not passing block note id, it uses recent notes`() = runTest {
        val now = LocalDateTime.now()
        notesRepositoryFake.setBlockNotesList(
            listOf(
                BlockNoteDomainModel(
                    id = 1,
                    name = "Default",
                    color = 100,
                    createdAt = now,
                    updatedAt = now
                )
            )
        )

        notesRepositoryFake.setRecentNotes(
            listOf(
                NoteDomainModel(
                    id = 1,
                    title = "Title 1",
                    note = "Body 1",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().first()
                ),
                NoteDomainModel(
                    id = 2,
                    title = "Title 2",
                    note = "Body 2",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().first()
                )
            )
        )

        advanceTimeBy(100)
        assertThat(viewModel.state.value.notes).hasSize(2)
    }

    @RepeatedTest(value = 100)
    fun `verify passing block note id, it returns the notes of that block note`() = runTest {
        val savedStateHandle = SavedStateHandle(
            mapOf(NotesConsts.BLOCK_NOTE_ID to 1L)
        )
        viewModel = NotesViewModel(
            notesRepositoryFake,
            savedStateHandle
        )
        val collectJob = CoroutineScope(Dispatchers.Main).launch {
            viewModel.state.collect()
        }

        val now = LocalDateTime.now()
        notesRepositoryFake.setBlockNotesList(
            listOf(
                BlockNoteDomainModel(
                    id = 1,
                    name = "Default",
                    color = 100,
                    createdAt = now,
                    updatedAt = now
                ),
                BlockNoteDomainModel(
                    id = 2,
                    name = "Block note 2",
                    color = 100,
                    createdAt = now,
                    updatedAt = now
                )
            )
        )

        notesRepositoryFake.setNotes(
            listOf(
                NoteDomainModel(
                    id = 1,
                    title = "Title 1",
                    note = "Body 1",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().first()
                ),
                NoteDomainModel(
                    id = 2,
                    title = "Title 2",
                    note = "Body 2",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().last()
                )
            )
        )

        advanceTimeBy(100)
        assertThat(viewModel.state.value.notes).hasSize(1)
        assertThat(viewModel.state.value.notes.first().blockNoteUiModel.id).isEqualTo(1)
        assertThat(viewModel.state.value.notes.first().id).isEqualTo(1L)

        collectJob.cancel()
    }

    @Test
    fun `verify updating order by of notes, updates the internal state`() = runTest {
        viewModel.onEvent(NotesEvent.OnOrderByChanged(OrderBy.Title))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.orderBy).isEqualTo(OrderBy.Title)

        viewModel.onEvent(NotesEvent.OnOrderByChanged(OrderBy.UpdatedAt(DateOrderType.OLDER)))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.orderBy).isEqualTo(OrderBy.UpdatedAt(DateOrderType.OLDER))

        viewModel.onEvent(NotesEvent.OnOrderByChanged(OrderBy.UpdatedAt(DateOrderType.RECENT)))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.orderBy).isEqualTo(OrderBy.UpdatedAt(DateOrderType.RECENT))

        viewModel.onEvent(NotesEvent.OnOrderByChanged(OrderBy.CreatedAt(DateOrderType.OLDER)))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.orderBy).isEqualTo(OrderBy.CreatedAt(DateOrderType.OLDER))

        viewModel.onEvent(NotesEvent.OnOrderByChanged(OrderBy.CreatedAt(DateOrderType.RECENT)))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.orderBy).isEqualTo(OrderBy.CreatedAt(DateOrderType.RECENT))
    }

    @RepeatedTest(100)
    fun `verify selecting notes, state is correctly update with selection`() = runTest {
        val now = LocalDateTime.now()
        notesRepositoryFake.setBlockNotesList(
            listOf(
                BlockNoteDomainModel(
                    id = 1,
                    name = "Default",
                    color = 100,
                    createdAt = now,
                    updatedAt = now
                )
            )
        )

        notesRepositoryFake.setRecentNotes(
            listOf(
                NoteDomainModel(
                    id = 1,
                    title = "Title 1",
                    note = "Body 1",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().first()
                ),
                NoteDomainModel(
                    id = 2,
                    title = "Title 2",
                    note = "Body 2",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().first()
                )
            )
        )

        advanceTimeBy(100)
        viewModel.onEvent(NotesEvent.OnSelectionChanged(
            id = 1,
            selected = true
        ))
        advanceTimeBy(100)

        var selectedNotes = savedStateHandle.get<List<Long>>(NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY)!!

        assertThat(selectedNotes).hasSize(1)

        advanceTimeBy(100)
        viewModel.onEvent(NotesEvent.OnSelectionChanged(
            id = 2,
            selected = true
        ))
        advanceTimeBy(100)

        selectedNotes = savedStateHandle.get<List<Long>>(NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY)!!

        assertThat(selectedNotes).hasSize(2)
    }

    @RepeatedTest(100)
    fun `verify removing all selected notes, disable multi selection`() = runTest {
        val now = LocalDateTime.now()
        notesRepositoryFake.setBlockNotesList(
            listOf(
                BlockNoteDomainModel(
                    id = 1,
                    name = "Default",
                    color = 100,
                    createdAt = now,
                    updatedAt = now
                )
            )
        )

        notesRepositoryFake.setRecentNotes(
            listOf(
                NoteDomainModel(
                    id = 1,
                    title = "Title 1",
                    note = "Body 1",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().first()
                ),
                NoteDomainModel(
                    id = 2,
                    title = "Title 2",
                    note = "Body 2",
                    createdAt = now,
                    updatedAt = now,
                    blockNote = notesRepositoryFake.blockNotes().first().first()
                )
            )
        )

        viewModel.onEvent(NotesEvent.MultiSelectionStateChanged(active = true, id = 1))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.isMultiSelectionActive).isTrue()
        viewModel.onEvent(NotesEvent.OnSelectionChanged(
            id = 2,
            selected = true
        ))
        advanceTimeBy(100)
        val selectedNotes = savedStateHandle.get<List<Long>>(NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY)!!
        assertThat(selectedNotes).hasSize(2)

        viewModel.onEvent(NotesEvent.OnSelectionChanged(
            id = 1,
            selected = false
        ))
        viewModel.onEvent(NotesEvent.OnSelectionChanged(
            id = 2,
            selected = false
        ))

        advanceTimeBy(100)
        assertThat(viewModel.state.value.isMultiSelectionActive).isFalse()
    }
}