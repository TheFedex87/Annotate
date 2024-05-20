package it.thefedex87.core_ui.utils

import it.thefedex87.core_ui.R
import it.thefedex87.core_ui.utils.UiText
import it.thefedex87.error_handling.DataError
import it.thefedex87.error_handling.Result

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Local.NOT_FOUND -> UiText.StringResource(
            R.string.block_note_not_found
        )

        DataError.Local.SAVE_INTO_DB_ERROR -> UiText.StringResource(
            R.string.error_saving_block_note_into_db
        )
        DataError.Local.REMOVE_FROM_DB_ERROR -> UiText.StringResource(
            R.string.error_deleting_block_note
        )
        else -> UiText.StringResource(
            R.string.unknown_error
        )
    }
}

fun Result.Error<*, DataError>.asErrorUiText():UiText {
    return error.asUiText()
}