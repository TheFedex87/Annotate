package it.thefedex87.notes_presentation.block_note

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.notes_domain.model.BlockNoteModel
import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.notes_domain.repository.NotesRepository
import it.thefedex87.notes_utils.NotesConsts
import it.thefedex87.utils.Consts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

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

    val state = combine(
        repository.blockNotes(),
        repository.notesPreferences
    ) { (blockNotes, notesPreferences) ->
        blockNotes as List<BlockNoteModel>
        notesPreferences as NotesPreferences
        Pair(blockNotes, notesPreferences)
    }.mapLatest { (blockNotes, notesPreferences)  ->
        BlockNotesState(
            blockNotes = blockNotes,
            visualizationType = notesPreferences.blockNotesVisualizationType
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        BlockNotesState()
    )


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
        viewModelScope.launch {
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

        }
    }

    fun onEvent(event: BlockNotesEvent) {
        viewModelScope.launch {
            when (event) {
                is BlockNotesEvent.OnBlockNoteClicked -> {
                    Log.d(Consts.TAG, "Clicked on block note: ${event.id}")
                }

                is BlockNotesEvent.OnQueryChanged -> {
                    savedStateHandle[NotesConsts.QUERY_SAVED_STATE_HANDLE_KEY] = event.query
                }

                is BlockNotesEvent.OnVisualizationTypeChanged -> {
                    repository.updateBlockNotesVisualizationType(event.visualizationType)
                }
            }
        }
    }
}