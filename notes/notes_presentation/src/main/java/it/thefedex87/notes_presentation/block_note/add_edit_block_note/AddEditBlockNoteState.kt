package it.thefedex87.notes_presentation.block_note.add_edit_block_note

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddEditBlockNoteState(
    val id: Long? = null,
    val showDialog: Boolean = false,
    val name: String = "",
    val selectedColor: Int = Color.Magenta.toArgb()
): Parcelable