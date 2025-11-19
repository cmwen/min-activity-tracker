package com.minactivitytracker.service

import android.app.usage.UsageStatsManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.minactivitytracker.data.entity.AppSession
import com.minactivitytracker.repository.ActivityRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

import com.minactivitytracker.repository.SettingsRepository
import kotlinx.coroutines.flow.first

@HiltWorker
class UsageTrackingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ActivityRepository,
    private val settingsRepository: SettingsRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        if (!settingsRepository.isUsageTrackingEnabled.first()) {
            return Result.success()
        }
        val usageStatsManager = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val startTime = endTime - 1000 * 60 * 15 // Last 15 minutes

        val events = usageStatsManager.queryEvents(startTime, endTime)
        val event = android.app.usage.UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == android.app.usage.UsageEvents.Event.MOVE_TO_FOREGROUND) {
                // Found a foreground event
                repository.insertSession(
                    AppSession(
                        packageName = event.packageName,
                        startTimestamp = event.timeStamp,
                        endTimestamp = event.timeStamp, // Placeholder, ideally we find the matching background event
                        durationMs = 0 // Placeholder
                    )
                )
            }
        }

        return Result.success()
    }
}
