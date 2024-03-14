package it.thefedex87.notes_domain.preferences

import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.core.domain.model.VisualizationType
import kotlinx.coroutines.flow.Flow

interface NotesPreferencesManager {
    fun preferencesFlow(): Flow<NotesPreferences>
    suspend fun updateBlockNotesVisualizationType(type: VisualizationType)
    suspend fun updateNotesVisualizationType(type: VisualizationType)
}