package com.rafih.justfocus.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("focus_mode_status")

    private val FOCUS_MODE_KEY = booleanPreferencesKey("focus_mode_enabled")

    val focusModeStatus: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FOCUS_MODE_KEY] ?: false
    }

    suspend fun setFocusModeStatus(enabled: Boolean){
        context.dataStore.edit {
            it[FOCUS_MODE_KEY] = enabled
        }
    }
}