package it.thefedex87.notes_presentation.note.screens

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
import it.thefedex87.core.utils.Quintuple
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.error_handling.Result
import it.thefedex87.logging.data.Logger
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_presentation.block_note.model.toBlockNoteUiModel
import it.thefedex87.notes_presentation.note.model.toNoteUiModel
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val savedStateHandle: SavedStateHandle,
    private val logger: Logger
) : ViewModel() {
    // This ViewModel is used for 2 different screen: note of a specific block note and
    // the screen of recent notes

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(
        NotesState()
    )

    private val _blockNote: MutableStateFlow<BlockNoteDomainModel?> = MutableStateFlow(null)

    @OptIn(FlowPreview::class)
    val state = combine(
        _blockNote.flatMapLatest {
            // If blocknote is null, this means we are in the screen recent notes (or that it is not still been loaded)
            if(it != null)
                repository.notes(it)
            else
                repository.recentNotes()
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
        val notesList = if(_blockNote.value != null) {
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
            sortedList
        } else {
            notes.map {
                it.toNoteUiModel(
                    isSelected = selectedNotes.contains(it.id)
                )
            }
        }

        state.copy(
            notes = notesList,
            blockNote = _blockNote.value,
            visualizationType = preferences.notesVisualizationType,
            isOrderByExpanded = state.isOrderByExpanded,
            orderBy = preferences.notesOrderBy,
            showOrderByCombo = _blockNote.value != null
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        NotesState()
    )

    init {
        // BlockNoteId is passed only when this view model is used in the screen
        // of notes of a specific block note, when we are in the screen of recent notes (that
        // uses always this viewmodel the blockNoteId is not passed since we are not referring
        // the notes of a specific blocknote
        val id = savedStateHandle.get<Long>(NotesConsts.BLOCK_NOTE_ID)
        logger.d(Consts.TAG, "New NotesOfBlockNoteViewModel block note id is: $id")
        viewModelScope.launch {
            val blockNote = repository.blockNotes().first().firstOrNull {
                it.id == id
            }
            if(blockNote != null) {
                _blockNote.update {
                    blockNote
                }
            }
        }
    }

    fun onEvent(event: NotesEvent) {
        viewModelScope.launch {
            when (event) {
                is NotesEvent.OnAddNewNoteClicked -> Unit
                is NotesEvent.OnNoteClicked -> Unit
                is NotesEvent.OnVisualizationTypeChanged -> {
                    repository.updateNotesVisualizationType(event.visualizationType)
                }

                is NotesEvent.OnOrderByChanged -> {
                    _state.update {
                        it.copy(
                            isOrderByExpanded = false
                        )
                    }
                    repository.updateNotesOrderBy(event.orderBy)
                }

                is NotesEvent.ExpandOrderByMenuChanged -> {
                    _state.update {
                        it.copy(
                            isOrderByExpanded = event.isExpanded
                        )
                    }
                }

                is NotesEvent.MultiSelectionStateChanged -> {
                    if (state.value.isMultiSelectionActive && event.active) return@launch

                    _state.update {
                        it.copy(
                            isMultiSelectionActive = event.active
                        )
                    }

                    savedStateHandle[NotesConsts.SELECTED_NOTES_SAVED_STATE_HANDLE_KEY] =
                        listOf(event.id)
                }

                is NotesEvent.DeselectAllNotes -> {
                    deselectAllNotes()
                }

                is NotesEvent.OnSelectionChanged -> {
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

                is NotesEvent.OnRemoveSelectedNotesClicked -> {
                    _state.update {
                        it.copy(
                            showConfirmDeleteDialog = true
                        )
                    }
                }

                is NotesEvent.OnRemoveSelectedNotesCanceled -> {
                    _state.update {
                        it.copy(
                            showConfirmDeleteDialog = false
                        )
                    }
                }

                is NotesEvent.OnRemoveSelectedNotesConfirmed -> {
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

                is NotesEvent.OnMoveNotesRequested -> {
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

                is NotesEvent.OnMoveNotesCanceled -> {
                    _state.update {
                        it.copy(
                            showMoveNotesDialog = false,
                            moveNotesBlockNotesExpanded = false,
                            selectedBlockNoteToMoveNotes = null
                        )
                    }
                }

                is NotesEvent.OnMoveNotesNewBlockNoteSelected -> {
                    _state.update {
                        it.copy(
                            selectedBlockNoteToMoveNotes = event.id,
                            moveNotesBlockNotesExpanded = false
                        )
                    }
                }

                is NotesEvent.ExpandMoveNotesBlockNotesList -> {
                    _state.update {
                        it.copy(
                            moveNotesBlockNotesExpanded = event.isExpanded
                        )
                    }
                }

                is NotesEvent.OnMoveNotesConfirmed -> {
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