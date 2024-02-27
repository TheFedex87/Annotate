package it.thefedex87.notes_presentation.block_note

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.utils.UiText
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_presentation.R
import it.thefedex87.notes_presentation.block_note.addBlockNote.AddBlockNoteEvent
import it.thefedex87.notes_presentation.block_note.addBlockNote.AddBlockNoteState
import it.thefedex87.notes_presentation.block_note.model.toBlockNoteUiModel
import it.thefedex87.notes_utils.NotesConsts
import it.thefedex87.utils.Consts
import it.thefedex87.utils.Quadruple
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BlockNotesViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    /*val query
        get() = _query.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            ""
        )

    val blockNotes = _query.flatMapLatest {
        repository.blockNotes(it)
    }.mapLatest { bn ->
        bn.toImmutableList()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        persistentListOf()
    )*/

    /*private val _query = savedStateHandle.getStateFlow(
        NotesConsts.QUERY_SAVED_STATE_HANDLE_KEY,
        ""
    )*/
    /*private val blockNotes = _query.flatMapLatest {
        repository.blockNotes(it).distinctUntilChanged()
    }*/

    private val _state = MutableStateFlow(
        BlockNotesState()
    )
    val state = combine(
        _state,
        repository.blockNotes(),
        repository.notesPreferences,
        savedStateHandle.getStateFlow(NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY, AddBlockNoteState())
    ) { state, blockNotes, notesPreferences, addBlockNotesState ->
        Quadruple(state, blockNotes, notesPreferences, addBlockNotesState)
    }.mapLatest { (state, blockNotes, notesPreferences, addBlockNotesState) ->
        state.copy(
            blockNotes = blockNotes.map {
                it.toBlockNoteUiModel(
                    state.showOptionsId == it.id
                )
            },
            visualizationType = notesPreferences.blockNotesVisualizationType,
            addBlockNoteState = addBlockNotesState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        BlockNotesState()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent
        .receiveAsFlow()


    /*val state = combine(
        blockNotes,
        _query
    ) { (blockNotes, query) ->
        blockNotes as List<BlockNoteDomainModel>
        query as String
        Pair(blockNotes, query)
    }.mapLatest { (blockNotes, query) ->
        Log.d(
            Consts.TAG,
            "Received list of block notes: ${blockNotes.map { it.id }} with hash ${blockNotes.hashCode()} and query: ${_query.value}"
        )
        BlockNotesState(
            blockNotes = blockNotes
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        BlockNotesState()
    )*/

    init {
        /*viewModelScope.launch {
            repository.removeAllBlockNotes()
        }

        viewModelScope.launch {
            var index = 1L
            while (index < 100) {
                repository.addBlockNote(
                    BlockNoteDomainModel(
                        id = index,
                        name = "Block note $index",
                        color = Color.argb(
                            255,
                            Random.nextInt(0, 255),
                            Random.nextInt(0, 255),
                            Random.nextInt(0, 255)
                        ),
                        createdAt = LocalDate.now(),
                        updatedAt = LocalDate.now()
                    )
                )
                delay(2000)
                index++
            }
        }*/
    }

    fun onAddBlockNoteEvent(event: AddBlockNoteEvent) {
        viewModelScope.launch {
            when (event) {
                is AddBlockNoteEvent.OnConfirmClicked -> {
                    val name = _state.value.addBlockNoteState.name
                    val color = _state.value.addBlockNoteState.selectedColor
                    val id = _state.value.addBlockNoteState.id

                    Log.d(Consts.TAG, "Adding/edit a new block note: $name")

                    try {
                        repository.addEditBlockNote(
                            BlockNoteDomainModel(
                                id = id,
                                name = name,
                                color = color,
                                createdAt = LocalDateTime.now(),
                                updatedAt = LocalDateTime.now()
                            )
                        )

                        savedStateHandle[NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] = AddBlockNoteState(
                            showDialog = false
                        )
                    } catch (ex: Exception) {
                        _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_adding_block_note)))
                    }

                }

                is AddBlockNoteEvent.OnDismiss -> {
                    savedStateHandle[NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] = AddBlockNoteState(
                        showDialog = false
                    )
                }

                is AddBlockNoteEvent.OnNameChanged -> {
                    savedStateHandle[NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                        savedStateHandle.get<AddBlockNoteState>(NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY)?.copy(
                            name = event.name
                        )
                }

                is AddBlockNoteEvent.OnSelectedNewColor -> {
                    savedStateHandle[NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                        savedStateHandle.get<AddBlockNoteState>(NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY)?.copy(
                            selectedColor = event.color.toArgb()
                        )
                }
            }
        }
    }

    fun onEvent(event: BlockNotesEvent) {
        viewModelScope.launch {
            when (event) {
                is BlockNotesEvent.OnBlockNoteClicked -> {
                    Log.d(Consts.TAG, "Clicked on block note: ${event.id}")
                }

                /*is BlockNotesEvent.OnQueryChanged -> {
                    savedStateHandle[NotesConsts.QUERY_SAVED_STATE_HANDLE_KEY] = event.query
                }*/

                is BlockNotesEvent.OnVisualizationTypeChanged -> {
                    repository.updateBlockNotesVisualizationType(event.visualizationType)
                }

                is BlockNotesEvent.OnAddNewBlockNoteClicked -> {
                    savedStateHandle[NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] = AddBlockNoteState(
                        showDialog = true
                    )
                }

                is BlockNotesEvent.OnShowBlockNoteOptionsClicked -> {
                    _state.update {
                        it.copy(
                            showOptionsId = event.id
                        )
                    }
                }

                is BlockNotesEvent.OnDismissBlockNoteOptions -> {
                    _state.update {
                        it.copy(
                            showOptionsId = null
                        )
                    }
                }

                is BlockNotesEvent.OnEditBlockNoteClicked -> {
                    repository.blockNotes().first().firstOrNull { it.id == event.id }
                        ?.let { blockNote ->
                            savedStateHandle[NotesConsts.EDITING_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] = AddBlockNoteState(
                                id = blockNote.id,
                                name = blockNote.name,
                                showDialog = true,
                                selectedColor = blockNote.color
                            )
                            _state.update {
                                it.copy(
                                    showOptionsId = null
                                )
                            }
                        }
                }

                is BlockNotesEvent.OnDeleteBlockNoteClicked -> {
                    repository.blockNotes().first().firstOrNull { it.id == event.id }
                        ?.let { blockNote ->
                            _state.update {
                                it.copy(
                                    blockNoteDeleteState = BlockNoteDeleteState(
                                        showDeleteDialog = true,
                                        deleteBlockNoteName = blockNote.name
                                    ),
                                    showOptionsId = null
                                )
                            }
                        }
                }

                is BlockNotesEvent.OnDeleteBlockNoteConfirmed -> {

                }

                is BlockNotesEvent.OnDeleteBlockNoteDismissed -> {
                    _state.update {
                        it.copy(
                            blockNoteDeleteState = BlockNoteDeleteState(
                                showDeleteDialog = false,
                                deleteBlockNoteName = ""
                            )
                        )
                    }
                }
            }
        }
    }
}