package it.thefedex87.core_ui.events

import it.thefedex87.core_ui.utils.UiText

sealed interface UiEvent {
    data class ShowSnackBar(val message: UiText) : UiEvent
    data class PopBackStack(val bundle: Map<String, String>? = null) : UiEvent
    //data class SaveBitmapLocal(val path: String) : UiEvent
    data class ScrollPagerToPage(val page: Int) : UiEvent
}