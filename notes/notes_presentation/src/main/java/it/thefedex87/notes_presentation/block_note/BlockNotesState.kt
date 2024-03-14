package it.thefedex87.notes_presentation.block_note

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_presentation.block_note.add_edit_block_note.AddEditBlockNoteState
import it.thefedex87.notes_presentation.block_note.model.BlockNoteUiModel
import kotlinx.parcelize.Parcelize

@Immutable
data class BlockNotesState(
    val blockNotes: List<BlockNoteUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val visualizationType: VisualizationType = VisualizationType.Grid,
    val addEditBlockNoteState: AddEditBlockNoteState = AddEditBlockNoteState(),
    val showOptionsId: Long? = null,
    val deleteBlockNoteState: DeleteBlockNoteState = DeleteBlockNoteState()
)


@Immutable
@Parcelize
data class DeleteBlockNoteState(
    val id: Long = 0,
    val showDialog: Boolean = false,
    val deleteBlockNoteName: String = ""
): Parcelable