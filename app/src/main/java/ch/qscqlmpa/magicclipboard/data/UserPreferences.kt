package ch.qscqlmpa.magicclipboard.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    private val context: Context
) {
    suspend fun toggleDarkTheme() {
        context.userPreferencesDataStore.edit { preferences ->
            val darkThemeEnabled = preferences[DARK_THEME_ENABLED] ?: false
            preferences[DARK_THEME_ENABLED] = !darkThemeEnabled
        }
    }

    fun darkThemeEnabled(): Flow<Boolean> = context.userPreferencesDataStore.data
        .map { preferences -> preferences[DARK_THEME_ENABLED] ?: false }
}

private val DARK_THEME_ENABLED = booleanPreferencesKey("darkThemeEnabled")

private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "userPreferences"
)
