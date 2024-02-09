package it.thefedex87.notes_presentation.block_note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.repository.AnnotateRepository
import it.thefedex87.utils.Consts
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockNotesViewModel @Inject constructor(
    repository: AnnotateRepository
): ViewModel() {
    val blockNotes = repository.blockNotes

    fun onEvent(event: BlockNotesEvent) {
        viewModelScope.launch {
            when(event) {
                is BlockNotesEvent.OnBlockNoteClicked -> {
                    Log.d(Consts.TAG, "Clicked on block note: ${event.id}")
                }
            }
        }
    }

}