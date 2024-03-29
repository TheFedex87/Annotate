package it.thefedex87.notes_data.repository

import assertk.assertThat
import assertk.assertions.isInstanceOf
import it.thefedex87.core.data.local.BlockNoteDao
import it.thefedex87.core.data.local.NoteDao
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_data.repository.MockitoHelper.anyObject
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDateTime

internal class NotesRepositoryImplTest {
    private lateinit var notesRepository: NotesRepositoryImpl

    private lateinit var blockNoteDao: BlockNoteDao
    private lateinit var noteDao: NoteDao
    private lateinit var preferencesManager: NotesPreferencesManager

    @BeforeEach
    fun SetUp() = runTest {
        blockNoteDao = spy(BlockNoteDao::class.java)
        noteDao = spy(NoteDao::class.java)
        preferencesManager = spy(NotesPreferencesManager::class.java)

        `when`(blockNoteDao.insertBlockNote(anyObject())).thenReturn(1)
        `when`(noteDao.insertNote(anyObject())).thenReturn(1)

        notesRepository = NotesRepositoryImpl(
            blockNoteDao,
            noteDao,
            preferencesManager
        )
    }

    @Test
    fun `calling updateBlockNotesVisualizationType, calls relative update in preferences manager with Grid visualization type`() =
        runTest {
            notesRepository.updateBlockNotesVisualizationType(VisualizationType.Grid)
            verify(preferencesManager).updateBlockNotesVisualizationType(VisualizationType.Grid)
        }

    @Test
    fun `calling updateBlockNotesVisualizationType, calls relative update in preferences manager with List visualization type`() =
        runTest {
            notesRepository.updateBlockNotesVisualizationType(VisualizationType.List)
            verify(preferencesManager).updateBlockNotesVisualizationType(VisualizationType.List)
        }

    @Test
    fun `calling updateNotesVisualizationType, calls relative update in preferences manager with Grid visualization type`() =
        runTest {
            notesRepository.updateNotesVisualizationType(VisualizationType.Grid)
            verify(preferencesManager).updateNotesVisualizationType(VisualizationType.Grid)
        }

    @Test
    fun `calling updateNotesVisualizationType, calls relative update in preferences manager with List visualization type`() =
        runTest {
            notesRepository.updateNotesVisualizationType(VisualizationType.List)
            verify(preferencesManager).updateNotesVisualizationType(VisualizationType.List)
        }

    @Test
    fun `check adding new block note, the insert method of dao is called and success is returned`() = runTest {
        val blockNote = BlockNoteDomainModel(
            id = null,
            name = "Default",
            color = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val result = notesRepository.addEditBlockNote(blockNote)

        assertThat(result).isInstanceOf<it.thefedex87.error_handling.Result.Success<*, *>>()
        verify(blockNoteDao).insertBlockNote(anyObject())
    }

    @Test
    fun `check updating block note, the update method of dao is called and success is returned`() = runTest {
        val blockNote = BlockNoteDomainModel(
            id = 1,
            name = "Default",
            color = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val result = notesRepository.addEditBlockNote(blockNote)

        assertThat(result).isInstanceOf<it.thefedex87.error_handling.Result.Success<*, *>>()
        verify(blockNoteDao).updateBlockNote(anyObject())
    }

    @Test
    fun `check adding new note, the insert method of dao is called and success is returned`() = runTest {
        val blockNote = NoteDomainModel(
            id = null,
            title = "Title",
            note = "Body note",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            blockNote = BlockNoteDomainModel(
                id = 1,
                name = "Default",
                color = 0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        val result = notesRepository.addEditNote(blockNote)

        assertThat(result).isInstanceOf<it.thefedex87.error_handling.Result.Success<*, *>>()
        verify(noteDao).insertNote(anyObject())
    }

    @Test
    fun `check updating note, the insert method of dao is called and success is returned`() = runTest {
        val blockNote = NoteDomainModel(
            id = 2,
            title = "Title",
            note = "Body note",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            blockNote = BlockNoteDomainModel(
                id = 1,
                name = "Default",
                color = 0,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        val result = notesRepository.addEditNote(blockNote)

        assertThat(result).isInstanceOf<it.thefedex87.error_handling.Result.Success<*, *>>()
        verify(noteDao).updateNote(anyObject())
    }

    @ParameterizedTest
    @ValueSource(
        longs = [1, 3, 5, 0]
    )
    fun `when move note is called, move note in dao is called for each element we want to move and success is returned`(noteToMove: Long) = runTest {
        val notesToMove = (1L..noteToMove).toList()

        val result = notesRepository.moveNotesToBlockNote(notesToMove, 1L)

        assertThat(result).isInstanceOf<it.thefedex87.error_handling.Result.Success<*, *>>()
        verify(noteDao, times(noteToMove.toInt())).moveNote(anyLong(), anyLong())
    }
}

object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T =  null as T
}