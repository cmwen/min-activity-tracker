package io.cmwen.min_activity_tracker.features.tracking

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.BatteryManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import io.cmwen.min_activity_tracker.data.preferences.UserPreferences
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataCollector
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val sessionRepository: SessionRepository,
        private val deviceEventRepository: DeviceEventRepository,
        private val batterySampleRepository: BatterySampleRepository,
    ) {
        private val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        private val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        companion object {
            private const val BATTERY_TEMP_SCALE = 10f
            private const val ONE_HOUR_MS = 60 * 60 * 1000L
        }

        suspend fun collectUsageData(
            startTime: Long,
            endTime: Long,
            preferences: UserPreferences,
        ) {
            if (!preferences.collectAppUsage) {
                return
            }
            val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
            val foregroundEvents = mutableMapOf<String, UsageEvents.Event>()
            val appSessions = mutableListOf<io.cmwen.min_activity_tracker.data.database.AppSessionEntity>()

            // Get current location (if permission granted)
            val currentLocation =
                if (preferences.collectLocation && !preferences.anonymizeLocationData) {
                    getCurrentLocationSafe()
                } else {
                    null
                }

            // Get current battery level
            val batteryLevel = if (preferences.collectBattery) getCurrentBatteryLevel() else null

            while (usageEvents.hasNextEvent()) {
                val event = UsageEvents.Event()
                usageEvents.getNextEvent(event)

                if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    foregroundEvents[event.packageName] = event
                } else if (event.eventType == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                    val startEvent = foregroundEvents.remove(event.packageName)
                    if (startEvent != null) {
                        val duration = event.timeStamp - startEvent.timeStamp
                        val appLabel = getAppLabel(event.packageName)
                        appSessions.add(
                            io.cmwen.min_activity_tracker.data.database.AppSessionEntity(
                                id = "${startEvent.packageName}-${startEvent.timeStamp}",
                                packageName = startEvent.packageName,
                                appLabel = appLabel,
                                startTimestamp = startEvent.timeStamp,
                                endTimestamp = event.timeStamp,
                                durationMs = duration,
                                startBatteryPct = batteryLevel,
                                endBatteryPct = if (preferences.collectBattery) getCurrentBatteryLevel() else null,
                                locationLatitude = currentLocation?.latitude,
                                locationLongitude = currentLocation?.longitude,
                                metadataJson = null,
                            ),
                        )
                    }
                }
            }

            sessionRepository.insertAll(appSessions)
        }

        private fun getAppLabel(packageName: String): String =
            try {
                val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
                context.packageManager.getApplicationLabel(appInfo).toString()
            } catch (e: PackageManager.NameNotFoundException) {
                packageName
            }

        suspend fun collectBatteryData(preferences: UserPreferences) {
            if (!preferences.collectBattery) {
                return
            }
            val batteryStatus: Intent? =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { iFilter ->
                    context.registerReceiver(null, iFilter)
                }

            val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = if (scale > 0) (level * 100 / scale.toFloat()).toInt() else -1

            val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val chargingState =
                when (status) {
                    BatteryManager.BATTERY_STATUS_CHARGING -> "CHARGING"
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> "DISCHARGING"
                    BatteryManager.BATTERY_STATUS_FULL -> "FULL"
                    BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "NOT_CHARGING"
                    else -> "UNKNOWN"
                }

            val temperature: Float? =
                batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)?.let {
                    if (it > 0) it / BATTERY_TEMP_SCALE else null
                }

            val timestamp = System.currentTimeMillis()
            val batterySample =
                io.cmwen.min_activity_tracker.data.database.BatterySampleEntity(
                    id = "battery-$timestamp",
                    timestamp = timestamp,
                    levelPercent = batteryPct,
                    chargingState = chargingState,
                    temperature = temperature,
                )

            batterySampleRepository.insert(batterySample)
        }

        /**
         * Get current battery level as percentage
         */
        private fun getCurrentBatteryLevel(): Int {
            val batteryStatus: Intent? =
                IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { iFilter ->
                    context.registerReceiver(null, iFilter)
                }

            val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            return if (scale > 0) (level * 100 / scale.toFloat()).toInt() else -1
        }

        /**
         * Get current location safely with error handling
         */
        private suspend fun getCurrentLocationSafe(): Location? =
            try {
                getCurrentLocation()
            } catch (e: SecurityException) {
                // Permission denied - this is expected when user hasn't granted location permission
                android.util.Log.d("DataCollector", "Location permission not granted")
                null
            } catch (e: IllegalStateException) {
                // Location services not available - this is expected on some devices/configurations
                android.util.Log.d("DataCollector", "Location services not available")
                null
            }

        /**
         * Get current location if permission is granted
         * Requires ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION
         */
        private suspend fun getCurrentLocation(): Location? =
            if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation.await()
            } else {
                null
            }

        /**
         * Collect comprehensive data snapshot at current time
         * Includes battery, location, and recent app usage
         */
        suspend fun collectDataSnapshot(preferences: UserPreferences) {
            val currentTime = System.currentTimeMillis()
            val oneHourAgo = currentTime - ONE_HOUR_MS

            // Collect battery data
            collectBatteryData(preferences)

            // Collect recent usage data
            collectUsageData(oneHourAgo, currentTime, preferences)
        }
    }
