package it.thefedex87.notes_presentation.block_note.addBlockNote

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddBlockNoteState(
    val id: Long? = null,
    val showDialog: Boolean = false,
    val name: String = "",
    val selectedColor: Int = Color.Magenta.toArgb()
): Parcelable