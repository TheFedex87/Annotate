package it.thefedex87.notes_presentation.block_note

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.logging.data.Logger
import it.thefedex87.notes_presentation.NotesRepositoryFake
import it.thefedex87.notes_presentation.block_note.add_edit_block_note.AddEditBlockNoteEvent
import it.thefedex87.notes_presentation.block_note.utils.MainCoroutineExtension
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
internal class BlockNotesViewModelTest {
    private lateinit var viewModel: BlockNotesViewModel
    private lateinit var notesRepositoryFake: NotesRepositoryFake

    private lateinit var collectJob: Job
    @BeforeEach
    fun setUp() {
        notesRepositoryFake = NotesRepositoryFake()
        val logger = Mockito.mock(Logger::class.java)

        viewModel = BlockNotesViewModel(
            notesRepositoryFake,
            SavedStateHandle(),
            logger
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
        viewModel.onEvent(BlockNotesEvent.OnVisualizationTypeChanged(VisualizationType.List))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.visualizationType).isEqualTo(VisualizationType.List)

        viewModel.onEvent(BlockNotesEvent.OnVisualizationTypeChanged(VisualizationType.Grid))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.visualizationType).isEqualTo(VisualizationType.Grid)
    }

    @Test
    fun `verify on add new block note request, the state to show dialog is correctly set to true`() = runTest {
        viewModel.onEvent(BlockNotesEvent.OnAddNewBlockNoteClicked)
        advanceTimeBy(100)
        assertThat(viewModel.state.value.addEditBlockNoteState.showDialog).isEqualTo(true)
    }

    @Test
    fun `verify on show options request, the state to show options is set with the correct id`() = runTest {
        viewModel.onEvent(BlockNotesEvent.OnShowBlockNoteOptionsClicked(1))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.showOptionsId).isEqualTo(1)

        viewModel.onEvent(BlockNotesEvent.OnShowBlockNoteOptionsClicked(2))
        advanceTimeBy(100)
        assertThat(viewModel.state.value.showOptionsId).isEqualTo(2)
    }

    @Test
    fun `verify on dismiss block note options, the state to show options is set to null`() = runTest {
        viewModel.onEvent(BlockNotesEvent.OnDismissBlockNoteOptions)
        advanceTimeBy(100)
        assertThat(viewModel.state.value.showOptionsId).isNull()
    }

    @Test
    fun `verify clicking on edit option, the state of editing is properly set and the state to show options is set to null`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnEditBlockNoteClicked(1))
        advanceTimeBy(100)

        assertThat(viewModel.state.value.showOptionsId).isNull()
        assertThat(viewModel.state.value.addEditBlockNoteState.id).isEqualTo(1)
        assertThat(viewModel.state.value.addEditBlockNoteState.name).isEqualTo("Default")
        assertThat(viewModel.state.value.addEditBlockNoteState.showDialog).isEqualTo(true)
        assertThat(viewModel.state.value.addEditBlockNoteState.selectedColor).isEqualTo(100)
    }

    @Test
    fun `verify clicking on remove option, the state of deleting is properly set and the state to show options is set to null`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnDeleteBlockNoteClicked(1))
        advanceTimeBy(100)

        assertThat(viewModel.state.value.showOptionsId).isNull()
        assertThat(viewModel.state.value.deleteBlockNoteState.id).isEqualTo(1)
        assertThat(viewModel.state.value.deleteBlockNoteState.deleteBlockNoteName).isEqualTo("Default")
        assertThat(viewModel.state.value.deleteBlockNoteState.showDialog).isEqualTo(true)
    }

    @Test
    fun `verify on delete block note confirmed, the element is removed and dialog is closed`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnDeleteBlockNoteClicked(1))
        advanceTimeBy(100)

        viewModel.onEvent(BlockNotesEvent.OnDeleteBlockNoteConfirmed)
        advanceTimeBy(100)

        assertThat(viewModel.state.value.blockNotes).isEmpty()
        assertThat(viewModel.state.value.deleteBlockNoteState.showDialog).isEqualTo(false)
    }

    @Test
    fun `verify on delete block note dismissed, hide the delete dialog`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnDeleteBlockNoteClicked(1))
        advanceTimeBy(100)

        viewModel.onEvent(BlockNotesEvent.OnDeleteBlockNoteDismissed)
        advanceTimeBy(100)

        assertThat(viewModel.state.value.deleteBlockNoteState.showDialog).isEqualTo(false)
        assertThat(viewModel.state.value.deleteBlockNoteState.deleteBlockNoteName).isEmpty()
    }

    @Test
    fun `verify on edit block note clicked, the options menu is closed and state is set properly`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnEditBlockNoteClicked(1))
        advanceTimeBy(100)

        assertThat(viewModel.state.value.showOptionsId).isNull()
        assertThat(viewModel.state.value.addEditBlockNoteState.id).isEqualTo(1)
        assertThat(viewModel.state.value.addEditBlockNoteState.name).isEqualTo("Default")
        assertThat(viewModel.state.value.addEditBlockNoteState.selectedColor).isEqualTo(100)
        assertThat(viewModel.state.value.addEditBlockNoteState.showDialog).isTrue()
    }

    @Test
    fun `verify removing blocknote is removed from repository`() = runTest {
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

        assertThat(notesRepositoryFake.blockNotes().first()).hasSize(1)

        viewModel.onEvent(BlockNotesEvent.OnDeleteBlockNoteClicked(1))
        advanceTimeBy(100)

        viewModel.onEvent(BlockNotesEvent.OnDeleteBlockNoteConfirmed)
        advanceTimeBy(100)

        assertThat(notesRepositoryFake.blockNotes().first()).hasSize(0)
    }

    @Test
    fun `verify dismissing block note edit, the dialog is hidden`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnEditBlockNoteClicked(1))
        advanceTimeBy(100)

        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnDismiss)
        advanceTimeBy(100)

        assertThat(viewModel.state.value.addEditBlockNoteState.showDialog).isFalse()
    }

    @Test
    fun `verify editing block note name, it is properly updated in the state`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnEditBlockNoteClicked(1))
        advanceTimeBy(100)

        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnNameChanged("New Name"))
        advanceTimeBy(100)

        assertThat(viewModel.state.value.addEditBlockNoteState.name).isEqualTo("New Name")
    }

    @Test
    fun `verify editing block note color, it is properly updated in the state`() = runTest {
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

        viewModel.onEvent(BlockNotesEvent.OnEditBlockNoteClicked(1))
        advanceTimeBy(100)

        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnSelectedNewColor(Color.Cyan))
        advanceTimeBy(100)

        assertThat(viewModel.state.value.addEditBlockNoteState.selectedColor).isEqualTo(Color.Cyan.toArgb())
    }

    @Test
    fun `verify editing block note, it is edited in the repository`() = runTest {
        val now = LocalDateTime.now()
        notesRepositoryFake.setBlockNotesList(
            listOf(
                BlockNoteDomainModel(
                    id = 1,
                    name = "Default",
                    color = Color.Green.toArgb(),
                    createdAt = now,
                    updatedAt = now
                )
            )
        )

        viewModel.onEvent(BlockNotesEvent.OnEditBlockNoteClicked(1))
        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnSelectedNewColor(Color.Cyan))
        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnNameChanged("New Name"))
        advanceTimeBy(100)

        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnConfirmClicked)
        advanceTimeBy(100)

        assertThat(viewModel.state.value.addEditBlockNoteState.showDialog).isFalse()
        assertThat(notesRepositoryFake.blockNotes().first().first().name).isEqualTo("New Name")
        assertThat(notesRepositoryFake.blockNotes().first().first().color).isEqualTo(Color.Cyan.toArgb())
    }

    @Test
    fun `verify adding block note, it is added in the repository`() = runTest {
        viewModel.onEvent(BlockNotesEvent.OnAddNewBlockNoteClicked)
        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnSelectedNewColor(Color.Cyan))
        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnNameChanged("New Name"))
        advanceTimeBy(100)

        viewModel.onAddBlockNoteEvent(AddEditBlockNoteEvent.OnConfirmClicked)
        advanceTimeBy(100)

        assertThat(viewModel.state.value.addEditBlockNoteState.showDialog).isFalse()
    }
}