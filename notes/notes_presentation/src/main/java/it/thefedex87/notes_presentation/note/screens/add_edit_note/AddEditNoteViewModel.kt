package it.thefedex87.notes_presentation.note.screens.add_edit_note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.error_handling.Result.*
import it.thefedex87.error_handling.Result.Error
import it.thefedex87.error_handling.Result.Success
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import it.thefedex87.error_handling.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiEvents = Channel<UiEvent>()
    val uiEvent = _uiEvents.receiveAsFlow()

    private val _state = MutableStateFlow(
        AddEditNoteState(
            noteState = TextFieldState(
                initialText = savedStateHandle[NotesConsts.ADD_EDIT_NOTE_BODY_SAVED_STATE_HANDLE_KEY]
                    ?: ""
            ),
            blockNoteId = savedStateHandle["blockNoteId"]!!,
            noteId = savedStateHandle[NotesConsts.NOTE_ID]
        )
    )

    private val savedStateHandleFlows = savedStateHandle.getStateFlow(
        NotesConsts.ADD_EDIT_NOTE_TITLE_SAVED_STATE_HANDLE_KEY,
        ""
    )

    val state = combine(
        _state,
        savedStateHandleFlows
    ) { state, title ->
        Pair(state, title)
    }.mapLatest { (state, title) ->
        AddEditNoteState(
            title = title,
            createdAt = state.createdAt,
            noteState = state.noteState
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        AddEditNoteState()
    )

    private var userEdit = false
    private var originalTitle = ""
    private var originalBody = ""

    init {
        _state.value.noteState.textAsFlow().onEach {
            if(!userEdit && originalBody != it.toString()) {
                userEdit = true
            }
            savedStateHandle[NotesConsts.ADD_EDIT_NOTE_BODY_SAVED_STATE_HANDLE_KEY] =
                it.toString()
            storeNote()
        }.launchIn(viewModelScope)

        _state.value.noteId?.let { noteId ->
            viewModelScope.launch {
                if (noteId > 0) {
                    repository.blockNotes().first()
                        .firstOrNull { it.id == _state.value.blockNoteId }
                        ?.let { blocknote ->
                            val note = repository.getNote(noteId, blocknote)
                            when(note) {
                                is Error -> {
                                    _uiEvents.send(UiEvent.ShowSnackBar(note.asErrorUiText()))
                                }
                                is Success -> {
                                    originalBody = note.data.body
                                    originalTitle = note.data.title

                                    _state.value.noteState.edit {
                                        replace(
                                            0,
                                            _state.value.noteState.text.length,
                                            note.data.body
                                        )
                                    }
                                    _state.update {
                                        it.copy(
                                            createdAt = note.data.createdAt
                                        )
                                    }
                                    savedStateHandle[NotesConsts.ADD_EDIT_NOTE_TITLE_SAVED_STATE_HANDLE_KEY] =
                                        note.data.title
                                }
                            }

                        }
                }
            }
        }

    }

    fun onEvent(event: AddEditNoteEvent) {
        viewModelScope.launch {
            when (event) {
                is AddEditNoteEvent.OnTitleChanged -> {
                    userEdit = true
                    savedStateHandle[NotesConsts.ADD_EDIT_NOTE_TITLE_SAVED_STATE_HANDLE_KEY] =
                        event.title
                    storeNote()
                }

                /*is AddEditNoteEvent.OnSaveNoteClicked -> {
                    repository.blockNotes().first()
                        .firstOrNull { it.id == _state.value.blockNoteId }?.let { blocknote ->
                            if (_state.value.noteId == null) {
                                // Adding note
                                repository.addEditNote(
                                    NoteDomainModel(
                                        id = null,
                                        title = savedStateHandle[NotesConsts.ADD_EDIT_NOTE_TITLE_SAVED_STATE_HANDLE_KEY]
                                            ?: "",
                                        body = _state.value.noteState.text.toString(),
                                        blockNote = blocknote,
                                        createdAt = _state.value.createdAt,
                                        updatedAt = LocalDateTime.now()
                                    )
                                )
                            } else {
                                // Editing note
                            }
                        }
                }*/
            }
        }
    }

    private suspend fun storeNote() {
        if(!userEdit) return

        val currentId = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)
        if (_state.value.noteState.text.isNotEmpty() ||
            savedStateHandle.get<String>(NotesConsts.ADD_EDIT_NOTE_TITLE_SAVED_STATE_HANDLE_KEY) != ""
        ) {
            repository.blockNotes().first().firstOrNull { it.id == _state.value.blockNoteId }
                ?.let { blocknote ->
                    val result = repository.addEditNote(
                        NoteDomainModel(
                            id = if (currentId == 0L) null else currentId,
                            title = savedStateHandle[NotesConsts.ADD_EDIT_NOTE_TITLE_SAVED_STATE_HANDLE_KEY]
                                ?: "",
                            body = _state.value.noteState.text.toString(),
                            blockNote = blocknote,
                            createdAt = _state.value.createdAt,
                            updatedAt = LocalDateTime.now()
                        )
                    )
                    when(result) {
                        is Error -> {
                            _uiEvents.send(
                                UiEvent.ShowSnackBar(
                                    result.asErrorUiText()
                                )
                            )
                        }
                        is Success -> {
                            savedStateHandle[NotesConsts.NOTE_ID] = result.data
                        }
                    }

                }
        } else {
            currentId?.let {
                if (it > 0) {
                    val result = repository.removeNote(it)
                    when(result) {
                        is Error -> {
                            _uiEvents.send(
                                UiEvent.ShowSnackBar(
                                    result.asErrorUiText()
                                )
                            )
                        }
                        is Success -> Unit
                    }
                }
            }
            savedStateHandle.remove<Long>(NotesConsts.NOTE_ID)
        }
    }
}