package com.minactivitytracker.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val IS_USAGE_TRACKING_ENABLED = booleanPreferencesKey("is_usage_tracking_enabled")
    private val IS_BATTERY_TRACKING_ENABLED = booleanPreferencesKey("is_battery_tracking_enabled")
    private val AUTO_EXPORT_INTERVAL = stringPreferencesKey("auto_export_interval")

    val isUsageTrackingEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_USAGE_TRACKING_ENABLED] ?: true
        }

    val isBatteryTrackingEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_BATTERY_TRACKING_ENABLED] ?: true
        }

    val autoExportInterval: Flow<AutoExportInterval> = context.dataStore.data
        .map { preferences ->
            val value = preferences[AUTO_EXPORT_INTERVAL] ?: AutoExportInterval.NONE.name
            try {
                AutoExportInterval.valueOf(value)
            } catch (e: IllegalArgumentException) {
                AutoExportInterval.NONE
            }
        }

    suspend fun setUsageTrackingEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_USAGE_TRACKING_ENABLED] = enabled
        }
    }

    suspend fun setBatteryTrackingEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_BATTERY_TRACKING_ENABLED] = enabled
        }
    }

    suspend fun setAutoExportInterval(interval: AutoExportInterval) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_EXPORT_INTERVAL] = interval.name
        }
    }

    fun getContext(): Context = context
}

enum class AutoExportInterval {
    NONE, DAILY, WEEKLY
}
