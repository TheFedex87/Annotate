package it.thefedex87.notes_presentation.note.screens.notes_of_block_note

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.DateOrderType
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.utils.Consts
import it.thefedex87.core.utils.Quadruple
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.error_handling.Result
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_presentation.block_note.model.toBlockNoteUiModel
import it.thefedex87.notes_presentation.note.model.toNoteUiModel
import it.thefedex87.notes_presentation.note.screens.asErrorUiText
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotesOfBlockNoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _blockNote: MutableStateFlow<BlockNoteDomainModel?> = MutableStateFlow(null)

    private val _state = MutableStateFlow(
        NotesOfBlockNoteState()
    )

    val state = combine(
        _blockNote.mapNotNull { it }.flatMapLatest {
            repository.notes(it)
        }.distinctUntilChanged(),
        _state,
        repository.notesPreferences,
        savedStateHandle.getStateFlow(
            NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY,
            initialValue = emptyList<Long>()
        )
    ) { notes, state, preferences, selectedNotes ->
        Quadruple(notes, state, preferences, selectedNotes)
    }.mapLatest { (notes, state, preferences, selectedNotes) ->
        val noteList = notes.map {
            it.toNoteUiModel(
                isSelected = selectedNotes.contains(it.id)
            )
        }
        val sortedList =
            when (preferences.notesOrderBy) {
                OrderBy.Title -> {
                    noteList.sortedBy { it.title.lowercase() }
                }

                OrderBy.CreatedAt(DateOrderType.RECENT) -> {
                    noteList.sortedBy { it.createdAt }.reversed()
                }

                OrderBy.CreatedAt(DateOrderType.OLDER) -> {
                    noteList.sortedBy { it.createdAt }
                }

                OrderBy.UpdatedAt(DateOrderType.RECENT) -> {
                    noteList.sortedBy { it.updatedAt }.reversed()
                }

                OrderBy.UpdatedAt(DateOrderType.OLDER) -> {
                    noteList.sortedBy { it.updatedAt }
                }

                else -> noteList.sortedBy { it.id }
            }

        state.copy(
            notes = sortedList,
            blockNote = _blockNote.value,
            visualizationType = preferences.notesVisualizationType,
            isOrderByExpanded = state.isOrderByExpanded,
            orderBy = preferences.notesOrderBy
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
                _blockNote.update {
                    blockNote
                }
            }
        }
    }

    fun onEvent(event: NotesOfBlockNoteEvent) {
        viewModelScope.launch {
            when (event) {
                is NotesOfBlockNoteEvent.OnAddNewNoteClicked -> Unit
                is NotesOfBlockNoteEvent.OnNoteClicked -> Unit
                is NotesOfBlockNoteEvent.OnVisualizationTypeChanged -> {
                    repository.updateNotesVisualizationType(event.visualizationType)
                }

                is NotesOfBlockNoteEvent.OnOrderByChanged -> {
                    _state.update {
                        it.copy(
                            isOrderByExpanded = false
                        )
                    }
                    repository.updateNotesOrderBy(event.orderBy)
                }

                is NotesOfBlockNoteEvent.ExpandOrderByMenuChanged -> {
                    _state.update {
                        it.copy(
                            isOrderByExpanded = event.isExpanded
                        )
                    }
                }

                is NotesOfBlockNoteEvent.MultiSelectionStateChanged -> {
                    if (state.value.isMultiSelectionActive && event.active) return@launch

                    _state.update {
                        it.copy(
                            isMultiSelectionActive = event.active
                        )
                    }

                    savedStateHandle[NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY] =
                        listOf(event.id)
                }

                is NotesOfBlockNoteEvent.DeselectAllNotes -> {
                    deselectAllNotes()
                }

                is NotesOfBlockNoteEvent.OnSelectionChanged -> {
                    savedStateHandle.get<List<Long>>(NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY)
                        ?.let { currentSelection ->
                            val mutableSelection = currentSelection.toMutableList()
                            if (event.selected) {
                                if (!mutableSelection.contains(event.id)) {
                                    mutableSelection.add(event.id)
                                }
                            } else {
                                if (mutableSelection.contains(event.id)) {
                                    mutableSelection.remove(event.id)
                                }
                            }
                            savedStateHandle[NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY] =
                                mutableSelection
                            if (mutableSelection.isEmpty()) {
                                _state.update {
                                    it.copy(
                                        isMultiSelectionActive = false
                                    )
                                }
                            }
                        }
                }

                is NotesOfBlockNoteEvent.OnRemoveSelectedNotesClicked -> {
                    _state.update {
                        it.copy(
                            showConfirmDeleteDialog = true
                        )
                    }
                }

                is NotesOfBlockNoteEvent.OnRemoveSelectedNotesCanceled -> {
                    _state.update {
                        it.copy(
                            showConfirmDeleteDialog = false
                        )
                    }
                }

                is NotesOfBlockNoteEvent.OnRemoveSelectedNotesConfirmed -> {
                    savedStateHandle.get<List<Long>>(NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY)
                        ?.let { selectedNotes ->
                            val result = repository.removeNotes(selectedNotes)
                            when (result) {
                                is Result.Error -> {
                                    _uiEvent.send(
                                        UiEvent.ShowSnackBar(
                                            result.asErrorUiText()
                                        )
                                    )
                                }

                                is Result.Success -> {
                                    deselectAllNotes()
                                }
                            }
                        }
                }

                is NotesOfBlockNoteEvent.OnMoveNotesRequested -> {
                    val blockNotes = repository.blockNotes().first().map {
                        it.toBlockNoteUiModel(false)
                    }
                    _state.update {
                        it.copy(
                            showMoveNotesDialog = true,
                            availableBlockNotes = blockNotes,
                            selectedBlockNoteToMoveNotes = repository.blockNotes().first().first().id
                        )
                    }
                }

                is NotesOfBlockNoteEvent.OnMoveNotesCanceled -> {
                    _state.update {
                        it.copy(
                            showMoveNotesDialog = false,
                            moveNotesBlockNotesExpanded = false,
                            selectedBlockNoteToMoveNotes = null
                        )
                    }
                }

                is NotesOfBlockNoteEvent.OnMoveNotesNewBlockNoteSelected -> {
                    _state.update {
                        it.copy(
                            selectedBlockNoteToMoveNotes = event.id,
                            moveNotesBlockNotesExpanded = false
                        )
                    }
                }

                is NotesOfBlockNoteEvent.ExpandMoveNotesBlockNotesList -> {
                    _state.update {
                        it.copy(
                            moveNotesBlockNotesExpanded = event.isExpanded
                        )
                    }
                }

                is NotesOfBlockNoteEvent.OnMoveNotesConfirmed -> {
                    repository.moveNotesToBlockNote(
                        notes = state.value.notes.filter { it.isSelected }.map { it.id },
                        blockNote = state.value.selectedBlockNoteToMoveNotes!!
                    )
                    _state.update {
                        it.copy(
                            moveNotesBlockNotesExpanded = false,
                            showMoveNotesDialog = false,
                            selectedBlockNoteToMoveNotes = null
                        )
                    }
                }
            }
        }
    }

    private fun deselectAllNotes() {
        _state.update {
            it.copy(
                isMultiSelectionActive = false,
                showConfirmDeleteDialog = false
            )
        }
        savedStateHandle[NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY] =
            emptyList<Long>()
    }
}