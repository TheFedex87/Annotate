package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.utils.Consts
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_presentation.note.model.toNoteUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotesOfBlockNoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _blockNote: MutableStateFlow<BlockNoteDomainModel?> = MutableStateFlow(null)

    private val _state = MutableStateFlow(
        NotesOfBlockNoteState()
    )

    val state = combine(
        _blockNote.mapNotNull { it }.flatMapLatest {
            repository.notes(it)
        }.distinctUntilChanged(),
        _state,
        repository.notesPreferences
    ) {notes, state, preferences ->
        Triple(notes, state, preferences)
    }.mapLatest { (notes, state, preferences) ->
        state.copy(
            notes = notes.map { it.toNoteUiModel() },
            blockNoteName = _blockNote.value?.name ?: "",
            visualizationType = preferences.notesVisualizationType
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        NotesOfBlockNoteState()
    )

    init {
        val id = savedStateHandle.get<Long>("blockNoteId")
        Log.d(Consts.TAG, "New NotesOfBlockNoteViewModel block note id is: $id")
        viewModelScope.launch {
            repository.blockNotes().first().firstOrNull {
                it.id == id
            }?.let { blockNote ->
                repository.removeAllNotes(blockNote.id!!)
                for(i in 1..20) {
                    var body = "This is teh body $i of my long long long note"
                    for(j in 1..Random.nextInt(100)) {
                        body += " note"
                    }

                    repository.addEditNote(
                        NoteDomainModel(
                            id = null,
                            title = "Title $i",
                            body = body,
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now(),
                            blockNote = blockNote
                        )
                    )
                }
                _blockNote.update {
                    blockNote
                }
            }
        }
    }

    fun onEvent(event: NotesOfBlockNoteEvent)  {
        viewModelScope.launch {
            when(event) {
                is NotesOfBlockNoteEvent.OnAddNewNoteClicked -> {
                    Unit
                }
                is NotesOfBlockNoteEvent.OnVisualizationTypeChanged -> {
                    repository.updateNotesVisualizationType(event.visualizationType)
                }
            }
        }
    }
}