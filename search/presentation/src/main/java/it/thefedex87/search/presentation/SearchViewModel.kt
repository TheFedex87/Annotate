@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")
@file:OptIn(ExperimentalFoundationApi::class)

package it.thefedex87.search.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.textAsFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel
import it.thefedex87.core_ui.events.UiEvent
import it.thefedex87.core_ui.utils.UiText
import it.thefedex87.core_ui.utils.asErrorUiText
import it.thefedex87.search.domain.SearchRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    val searchRepository: SearchRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _uiEvents = Channel<UiEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    init {
        _state.value.searchText.textAsFlow()
            .onEach {
                if (it.toString().isNotEmpty()) {
                    filter(it.toString())
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            foundNotes = emptyList(),
                            foundBlockNotes = emptyList()
                        )
                    }
                }
            }
            .debounce(500)
            .launchIn(viewModelScope)
    }

    private fun filter(filter: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            when (val notes = searchRepository.filterNotes(filter)) {
                is it.thefedex87.error_handling.Result.Error -> {
                    _uiEvents.send(UiEvent.ShowSnackBar(notes.asErrorUiText()))
                }

                is it.thefedex87.error_handling.Result.Success -> {
                    when (val blockNotes = searchRepository.filterBlockNotes(filter)) {
                        is it.thefedex87.error_handling.Result.Error -> {
                            _uiEvents.send(UiEvent.ShowSnackBar(blockNotes.asErrorUiText()))
                        }

                        is it.thefedex87.error_handling.Result.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    foundNotes = notes.data,
                                    foundBlockNotes = blockNotes.data
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}