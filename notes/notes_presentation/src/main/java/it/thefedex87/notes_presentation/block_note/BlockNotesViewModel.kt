package it.thefedex87.notes_presentation.block_note

import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.utils.Consts
import it.thefedex87.core.utils.Quadruple
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.utils.UiText
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_presentation.R
import it.thefedex87.notes_presentation.block_note.addEditBlockNote.AddEditBlockNoteEvent
import it.thefedex87.notes_presentation.block_note.addEditBlockNote.AddEditBlockNoteState
import it.thefedex87.notes_presentation.block_note.model.toBlockNoteUiModel
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val savedStateHandleFlows = combine(
        savedStateHandle.getStateFlow(
            NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY,
            AddEditBlockNoteState()
        ),
        savedStateHandle.getStateFlow(
            NotesConsts.DELETE_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY,
            DeleteBlockNoteState()
        )
    ) { addEditBlockNoteState, deleteBlockNoteState ->
        Pair(addEditBlockNoteState, deleteBlockNoteState)
    }.distinctUntilChanged()

    private val _state = MutableStateFlow(
        BlockNotesState()
    )
    val state = combine(
        _state,
        repository.blockNotes().distinctUntilChanged(),
        repository.notesPreferences,
        savedStateHandleFlows
    ) { state, blockNotes, notesPreferences, savedStateHandleFlows ->
        Quadruple(state, blockNotes, notesPreferences, savedStateHandleFlows)
    }.mapLatest { (state, blockNotes, notesPreferences, savedStateHandleFlows) ->
        val (addEditBlockNoteState, deleteBlockNoteState) = savedStateHandleFlows

        state.copy(
            blockNotes = blockNotes.map {
                it.toBlockNoteUiModel(
                    state.showOptionsId == it.id
                )
            },
            visualizationType = notesPreferences.blockNotesVisualizationType,
            addEditBlockNoteState = addEditBlockNoteState,
            deleteBlockNoteState = deleteBlockNoteState,
            showOptionsId = state.showOptionsId

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

    fun onAddBlockNoteEvent(event: AddEditBlockNoteEvent) {
        viewModelScope.launch {
            when (event) {
                is AddEditBlockNoteEvent.OnConfirmClicked -> {
                    savedStateHandle.get<AddEditBlockNoteState>(NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY)
                        ?.let { addEditBlockNoteState ->
                            val name = addEditBlockNoteState.name
                            val color = addEditBlockNoteState.selectedColor
                            val id = addEditBlockNoteState.id

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

                                savedStateHandle[NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                                    AddEditBlockNoteState(
                                        showDialog = false
                                    )
                            } catch (ex: Exception) {
                                if (id == null) {
                                    _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_adding_block_note)))
                                } else {
                                    _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_editing_block_note)))
                                }
                            }
                        }
                }

                is AddEditBlockNoteEvent.OnDismiss -> {
                    savedStateHandle[NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                        AddEditBlockNoteState(
                            showDialog = false
                        )
                }

                is AddEditBlockNoteEvent.OnNameChanged -> {
                    savedStateHandle[NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                        savedStateHandle.get<AddEditBlockNoteState>(NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY)
                            ?.copy(
                                name = event.name
                            )
                }

                is AddEditBlockNoteEvent.OnSelectedNewColor -> {
                    savedStateHandle[NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                        savedStateHandle.get<AddEditBlockNoteState>(NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY)
                            ?.copy(
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
                    Log.d(Consts.TAG, "Clicked on block note: ${event.blockNote.id}")
                }

                /*is BlockNotesEvent.OnQueryChanged -> {
                    savedStateHandle[NotesConsts.QUERY_SAVED_STATE_HANDLE_KEY] = event.query
                }*/

                is BlockNotesEvent.OnVisualizationTypeChanged -> {
                    repository.updateBlockNotesVisualizationType(event.visualizationType)
                }

                is BlockNotesEvent.OnAddNewBlockNoteClicked -> {
                    savedStateHandle[NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                        AddEditBlockNoteState(
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
                            savedStateHandle[NotesConsts.ADD_EDIT_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                                AddEditBlockNoteState(
                                    id = blockNote.id,
                                    name = blockNote.name,
                                    showDialog = true,
                                    selectedColor = blockNote.color
                                )
                        }
                    _state.update {
                        it.copy(
                            showOptionsId = null
                        )
                    }
                }

                is BlockNotesEvent.OnDeleteBlockNoteClicked -> {
                    repository.blockNotes().first().firstOrNull { it.id == event.id }
                        ?.let { blockNote ->
                            savedStateHandle[NotesConsts.DELETE_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                                DeleteBlockNoteState(
                                    id = blockNote.id!!,
                                    showDialog = true,
                                    deleteBlockNoteName = blockNote.name
                                )
                        }
                    _state.update {
                        it.copy(
                            showOptionsId = null
                        )
                    }
                }

                is BlockNotesEvent.OnDeleteBlockNoteConfirmed -> {
                    savedStateHandle.get<DeleteBlockNoteState>(NotesConsts.DELETE_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY)
                        ?.let { deleteBlockNoteState ->
                            val id = deleteBlockNoteState.id

                            repository.blockNotes().first().firstOrNull {
                                it.id == id
                            }?.let { blockNote ->
                                Log.d(Consts.TAG, "Delete block note: ${blockNote.name}")

                                try {
                                    repository.removeBlockNote(
                                        blockNote
                                    )

                                    savedStateHandle[NotesConsts.DELETE_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                                        DeleteBlockNoteState(
                                            showDialog = false
                                        )
                                } catch (ex: Exception) {
                                    _uiEvent.send(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_deleting_block_note)))
                                }
                            }
                        }
                }

                is BlockNotesEvent.OnDeleteBlockNoteDismissed -> {
                    savedStateHandle[NotesConsts.DELETE_BLOCK_NOTE_SAVED_STATE_HANDLE_KEY] =
                        DeleteBlockNoteState(
                            showDialog = false,
                            deleteBlockNoteName = ""
                        )
                }
            }
        }
    }
}