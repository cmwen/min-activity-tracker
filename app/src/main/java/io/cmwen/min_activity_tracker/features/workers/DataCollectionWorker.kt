package io.cmwen.min_activity_tracker.features.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.cmwen.min_activity_tracker.features.tracking.DataCollector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * WorkManager worker for periodic data collection
 * Runs in background to collect app usage, battery samples, and device events
 */
@HiltWorker
class DataCollectionWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataCollector: DataCollector
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val currentTime = System.currentTimeMillis()
            val lastHour = currentTime - (60 * 60 * 1000L) // Last hour
            
            // Collect app usage data from last hour
            dataCollector.collectUsageData(lastHour, currentTime)
            
            // Collect current battery sample
            dataCollector.collectBatteryData()
            
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
