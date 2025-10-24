package io.cmwen.min_activity_tracker.features.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.cmwen.min_activity_tracker.data.preferences.UserPreferencesRepository
import io.cmwen.min_activity_tracker.features.tracking.DataCollector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

/**
 * WorkManager worker for periodic data collection
 * Runs in background to collect app usage, battery samples, and device events
 */
@HiltWorker
class DataCollectionWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val dataCollector: DataCollector,
        private val userPreferencesRepository: UserPreferencesRepository,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result =
            withContext(Dispatchers.IO) {
                try {
                    val preferences = userPreferencesRepository.preferencesFlow.first()
                    if (!preferences.collectAppUsage && !preferences.collectBattery) {
                        return@withContext Result.success()
                    }

                    val currentTime = System.currentTimeMillis()
                    val lastHour = currentTime - (60 * 60 * 1000L) // Last hour

                    // Collect app usage data from last hour
                    dataCollector.collectUsageData(lastHour, currentTime, preferences)

                    // Collect current battery sample
                    dataCollector.collectBatteryData(preferences)

                    Result.success()
                } catch (e: Exception) {
                    // Retry on failure, with exponential backoff
                    if (runAttemptCount < MAX_RETRIES) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
            }

        companion object {
            private const val MAX_RETRIES = 3
            const val WORK_NAME = "data_collection_work"
        }
    }
