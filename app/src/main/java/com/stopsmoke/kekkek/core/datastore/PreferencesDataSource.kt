package com.stopsmoke.kekkek.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PreferencesDataSource @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
) {
    fun isOnboardingComplete(): Flow<Boolean> {
        return preferencesDataStore.data
            .map { preferences ->
                preferences[ONBOARDING_COMPLETE_KEY] ?: false
            }
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETE_KEY] = complete
        }
    }

    suspend fun setFirstRunning(isFirst: Boolean) {
        preferencesDataStore.edit { preferences ->
            preferences[IS_FIRST_RUNNING] = isFirst
        }
    }

    fun isFirstRunning(): Flow<Boolean> =
        preferencesDataStore.data
            .map { preferences ->
                preferences[IS_FIRST_RUNNING] ?: true
            }

    suspend fun clearAll() {
        preferencesDataStore.edit {
            it.clear()
        }
    }

    companion object {
        private val ONBOARDING_COMPLETE_KEY = booleanPreferencesKey("onboarding_complete")
        private val IS_FIRST_RUNNING = booleanPreferencesKey("is_first_running")
    }
}