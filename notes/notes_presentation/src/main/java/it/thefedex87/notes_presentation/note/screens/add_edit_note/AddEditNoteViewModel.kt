package it.thefedex87.notes_presentation.note.screens.add_edit_note

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core.utils.Consts
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.error_handling.Result.Error
import it.thefedex87.error_handling.Result.Success
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject
import it.thefedex87.notes_presentation.note.screens.asErrorUiText
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
            /*note = TextFieldState(
                initialText = savedStateHandle[NotesConsts.ADD_EDIT_NOTE_BODY_SAVED_STATE_HANDLE_KEY]
                    ?: ""
            ),*/
            blockNoteId = savedStateHandle[NotesConsts.BLOCK_NOTE_ID]!!,
            noteId = savedStateHandle[NotesConsts.NOTE_ID]
        )
    )

    val state = _state.mapLatest { state ->
        AddEditNoteState(
            title = state.title,
            note = state.note,
            createdAt = state.createdAt,
            blockNoteName = state.blockNoteName
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        AddEditNoteState()
    )

    private var originalTitle = ""
    private var originalBody = ""

    init {
        _state.value.noteId?.let { noteId ->
            viewModelScope.launch {
                if (noteId > 0) {
                    repository.blockNotes().first()
                        .firstOrNull { it.id == _state.value.blockNoteId }
                        ?.let { blocknote ->
                            val note = repository.getNote(noteId, blocknote)
                            Log.d(Consts.TAG, "Reading note on process death from repo: $note")
                            when (note) {
                                is Error -> {
                                    _uiEvents.send(UiEvent.ShowSnackBar(note.asErrorUiText()))
                                }

                                is Success -> {
                                    originalBody = note.data.note
                                    originalTitle = note.data.title

                                    _state.update {
                                        it.copy(
                                            createdAt = note.data.createdAt,
                                            blockNoteName = blocknote.name,
                                            note = note.data.note,
                                            title = note.data.title
                                        )
                                    }
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
                    _state.update {
                        it.copy(
                            title = event.title
                        )
                    }
                    storeNote()
                }

                is AddEditNoteEvent.OnNoteChanged -> {
                    _state.update {
                        it.copy(
                            note = event.note
                        )
                    }
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
        val currentId = savedStateHandle.get<Long>(NotesConsts.NOTE_ID)
        if (_state.value.note != "" ||
            _state.value.title != ""
        ) {
            repository.blockNotes().first().firstOrNull { it.id == _state.value.blockNoteId }
                ?.let { blocknote ->
                    Log.d(Consts.TAG, "Storing note on process death")
                    val result = repository.addEditNote(
                        NoteDomainModel(
                            id = if (currentId == 0L) null else currentId,
                            title = _state.value.title,
                            note = _state.value.note,
                            blockNote = blocknote,
                            createdAt = _state.value.createdAt,
                            updatedAt = LocalDateTime.now()
                        )
                    )
                    when (result) {
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
                    when (val result = repository.removeNote(it)) {
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