package it.thefedex87.core_ui.utils

import android.content.Context

sealed interface UiText {
    data class DynamicString(val text: String): UiText
    data class StringResource(val resId: Int): UiText
    data object Empty: UiText

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
            is Empty -> ""
        }
    }
}