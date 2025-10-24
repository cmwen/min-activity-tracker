package io.cmwen.min_activity_tracker.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.cmwen.min_activity_tracker.features.export.ExportFormat
import io.cmwen.min_activity_tracker.features.export.ExportTimeRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) {
        val preferencesFlow: Flow<UserPreferences> =
            dataStore
                .data
                .catch { emit(emptyPreferences()) }
                .map { preferences ->
                    UserPreferences(
                        collectAppUsage = preferences[COLLECT_APP_USAGE] ?: true,
                        collectBattery = preferences[COLLECT_BATTERY] ?: true,
                        collectLocation = preferences[COLLECT_LOCATION] ?: false,
                        collectActivityRecognition = preferences[COLLECT_ACTIVITY] ?: false,
                        anonymizeLocationData = preferences[ANONYMIZE_LOCATION] ?: false,
                        autoExportEnabled = preferences[AUTO_EXPORT_ENABLED] ?: false,
                        autoExportFormat =
                            preferences[AUTO_EXPORT_FORMAT]
                                ?.let { runCatching { ExportFormat.valueOf(it) }.getOrNull() }
                                ?: ExportFormat.JSON,
                        autoExportRange =
                            preferences[AUTO_EXPORT_RANGE]
                                ?.let { runCatching { ExportTimeRange.valueOf(it) }.getOrNull() }
                                ?: ExportTimeRange.LAST_24_HOURS,
                        autoExportAnonymize = preferences[AUTO_EXPORT_ANONYMIZE] ?: false,
                    )
                }

        suspend fun setCollectAppUsage(enabled: Boolean) {
            dataStore.edit { prefs ->
                prefs[COLLECT_APP_USAGE] = enabled
            }
        }

        suspend fun setCollectBattery(enabled: Boolean) {
            dataStore.edit { prefs ->
                prefs[COLLECT_BATTERY] = enabled
            }
        }

        suspend fun setCollectLocation(enabled: Boolean) {
            dataStore.edit { prefs ->
                prefs[COLLECT_LOCATION] = enabled
            }
        }

        suspend fun setCollectActivityRecognition(enabled: Boolean) {
            dataStore.edit { prefs ->
                prefs[COLLECT_ACTIVITY] = enabled
            }
        }

        suspend fun setAnonymizeLocation(enabled: Boolean) {
            dataStore.edit { prefs ->
                prefs[ANONYMIZE_LOCATION] = enabled
            }
        }

        suspend fun setAutoExportEnabled(enabled: Boolean) {
            dataStore.edit { prefs ->
                prefs[AUTO_EXPORT_ENABLED] = enabled
            }
        }

        suspend fun setAutoExportFormat(format: ExportFormat) {
            dataStore.edit { prefs ->
                prefs[AUTO_EXPORT_FORMAT] = format.name
            }
        }

        suspend fun setAutoExportRange(range: ExportTimeRange) {
            dataStore.edit { prefs ->
                prefs[AUTO_EXPORT_RANGE] = range.name
            }
        }

        suspend fun setAutoExportAnonymize(enabled: Boolean) {
            dataStore.edit { prefs ->
                prefs[AUTO_EXPORT_ANONYMIZE] = enabled
            }
        }

        companion object {
            private val COLLECT_APP_USAGE = booleanPreferencesKey("collect_app_usage")
            private val COLLECT_BATTERY = booleanPreferencesKey("collect_battery")
            private val COLLECT_LOCATION = booleanPreferencesKey("collect_location")
            private val COLLECT_ACTIVITY = booleanPreferencesKey("collect_activity_recognition")
            private val ANONYMIZE_LOCATION = booleanPreferencesKey("anonymize_location")
            private val AUTO_EXPORT_ENABLED = booleanPreferencesKey("auto_export_enabled")
            private val AUTO_EXPORT_FORMAT = stringPreferencesKey("auto_export_format")
            private val AUTO_EXPORT_RANGE = stringPreferencesKey("auto_export_range")
            private val AUTO_EXPORT_ANONYMIZE = booleanPreferencesKey("auto_export_anonymize")

        }
    }
