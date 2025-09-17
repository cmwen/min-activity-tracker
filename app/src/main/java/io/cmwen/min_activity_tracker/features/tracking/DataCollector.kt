package io.cmwen.min_activity_tracker.features.tracking

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.IntentFilter
import android.os.BatteryManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataCollector @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionRepository: SessionRepository,
    private val deviceEventRepository: DeviceEventRepository,
    private val batterySampleRepository: BatterySampleRepository
) {

    private val usageStatsManager =
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

    suspend fun collectUsageData(startTime: Long, endTime: Long) {
        val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
        val foregroundEvents = mutableMapOf<String, UsageEvents.Event>()
        val appSessions = mutableListOf<io.cmwen.min_activity_tracker.data.database.AppSessionEntity>()

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
                            durationMs = duration
                        )
                    )
                }
            }
        }

        sessionRepository.insertAll(appSessions)
    }

    private fun getAppLabel(packageName: String): String {
        return try {
            val appInfo = context.packageManager.getApplicationInfo(packageName, 0)
            context.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    suspend fun collectBatteryData() {
        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { iFilter ->
            context.registerReceiver(null, iFilter)
        }

        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = (level * 100 / scale.toFloat()).toInt()

        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        val chargingState = when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "CHARGING"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "DISCHARGING"
            BatteryManager.BATTERY_STATUS_FULL -> "FULL"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "NOT_CHARGING"
            else -> "UNKNOWN"
        }

        val timestamp = System.currentTimeMillis()
        val batterySample = io.cmwen.min_activity_tracker.data.database.BatterySampleEntity(
            id = "battery-${timestamp}",
            timestamp = timestamp,
            levelPercent = batteryPct,
            chargingState = chargingState
        )

        batterySampleRepository.insert(batterySample)
    }
}
