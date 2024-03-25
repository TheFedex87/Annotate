package it.thefedex87.notes_presentation.note.screens

import it.thefedex87.core_ui.utils.UiText
import it.thefedex87.error_handling.DataError
import it.thefedex87.error_handling.Result
import it.thefedex87.notes_presentation.R

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Local.NOT_FOUND -> UiText.StringResource(
            R.string.note_not_found
        )
        DataError.Local.SAVE_INTO_DB_ERROR -> UiText.StringResource(
            R.string.error_saving_note_into_db
        )
        DataError.Local.REMOVE_FROM_DB_ERROR -> UiText.StringResource(
            R.string.error_removing_note_from_db
        )
    }
}

fun Result.Error<*, DataError>.asErrorUiText():UiText {
    return error.asUiText()
}