package it.thefedex87.search.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text2.input.TextFieldState
import it.thefedex87.core.domain.model.BlockNoteDomainModel
import it.thefedex87.core.domain.model.NoteDomainModel

@OptIn(ExperimentalFoundationApi::class)
data class SearchState(
    val searchText: TextFieldState = TextFieldState(),
    val foundBlockNotes: List<BlockNoteDomainModel> = emptyList(),
    val foundNotes: List<NoteDomainModel> = emptyList(),
    val isLoading: Boolean = false
)