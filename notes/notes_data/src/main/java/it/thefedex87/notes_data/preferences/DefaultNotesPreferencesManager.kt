package it.thefedex87.notes_data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import it.thefedex87.core.domain.model.OrderBy
import it.thefedex87.core.domain.model.VisualizationType
import it.thefedex87.notes_domain.model.NotesPreferences
import it.thefedex87.notes_domain.preferences.NotesPreferencesManager
import it.thefedex87.notes_utils.NotesConsts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DefaultNotesPreferencesManager(
    @ApplicationContext private val context: Context
) : NotesPreferencesManager {
    companion object {
        val BLOCK_NOTES_VISUALIZATION_TYPE_KEY =
            stringPreferencesKey(NotesConsts.BLOCK_NOTES_VISUALIZATION_TYPE_PREFERENCE_KEY)
        val NOTES_VISUALIZATION_TYPE_KEY =
            stringPreferencesKey(NotesConsts.NOTES_VISUALIZATION_TYPE_PREFERENCE_KEY)
        val NOTES_ORDER_BY_TYPE_KEY =
            stringPreferencesKey(NotesConsts.NOTES_ORDER_BY_TYPE_PREFERENCE_KEY)

        const val DEFAULT_VISUALIZATION_TYPE_KEY = "Grid"
        const val DEFAULT_NOTE_ORDER_BY = "CreatedRecent"
    }

    private val Context.dataStore by preferencesDataStore(
        name = NotesConsts.NOTES_PREFERENCES_NAME
    )

    override fun preferencesFlow(): Flow<NotesPreferences> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
            throw exception
        }
        .map { preferences ->
            val blockNotesVisualizationType = VisualizationType.fromString(
                preferences[BLOCK_NOTES_VISUALIZATION_TYPE_KEY]
                    ?: DEFAULT_VISUALIZATION_TYPE_KEY
            )

            val notesVisualizationType = VisualizationType.fromString(
                preferences[NOTES_VISUALIZATION_TYPE_KEY]
                    ?: DEFAULT_VISUALIZATION_TYPE_KEY
            )

            val notesOrderBy = OrderBy.fromString(
                preferences[NOTES_ORDER_BY_TYPE_KEY]
                    ?: DEFAULT_NOTE_ORDER_BY
            )

            NotesPreferences(
                blockNotesVisualizationType = blockNotesVisualizationType,
                notesVisualizationType = notesVisualizationType,
                notesOrderBy = notesOrderBy
            )
        }

    override suspend fun updateBlockNotesVisualizationType(type: VisualizationType) {
        context.dataStore.edit { preferences ->
            preferences[BLOCK_NOTES_VISUALIZATION_TYPE_KEY] = VisualizationType.toSimpleString(type)
        }
    }

    override suspend fun updateNotesVisualizationType(type: VisualizationType) {
        context.dataStore.edit { preferences ->
            preferences[NOTES_VISUALIZATION_TYPE_KEY] = VisualizationType.toSimpleString(type)
        }
    }

    override suspend fun updateNotesOrderBy(orderBy: OrderBy) {
        context.dataStore.edit { preferences ->
            preferences[NOTES_ORDER_BY_TYPE_KEY] = OrderBy.toSimpleString(orderBy)
        }
    }
}