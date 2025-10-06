package io.cmwen.min_activity_tracker.features.workers

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages scheduling of background workers for data collection and analysis
 */
@Singleton
class WorkScheduler @Inject constructor(
    private val context: Context
) {
    private val workManager = WorkManager.getInstance(context)
    
    /**
     * Schedule periodic data collection worker
     * Runs every 15 minutes (minimum allowed by WorkManager)
     */
    fun scheduleDataCollection() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        
        val workRequest = PeriodicWorkRequestBuilder<DataCollectionWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            DataCollectionWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
    
    /**
     * Schedule daily analysis report generation
     * Runs once per day at midnight
     */
    fun scheduleDailyAnalysis() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        
        val inputData = workDataOf(AnalysisWorker.KEY_REPORT_TYPE to "daily")
        
        val workRequest = PeriodicWorkRequestBuilder<AnalysisWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInputData(inputData)
            .setInitialDelay(calculateDelayUntilMidnight(), TimeUnit.MILLISECONDS)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            "${AnalysisWorker.WORK_NAME}_daily",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
    
    /**
     * Schedule weekly analysis report generation
     * Runs once per week on Sunday at midnight
     */
    fun scheduleWeeklyAnalysis() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        
        val inputData = workDataOf(AnalysisWorker.KEY_REPORT_TYPE to "weekly")
        
        val workRequest = PeriodicWorkRequestBuilder<AnalysisWorker>(
            repeatInterval = 7,
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
        
        workManager.enqueueUniquePeriodicWork(
            "${AnalysisWorker.WORK_NAME}_weekly",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
    
    /**
     * Cancel all scheduled workers
     */
    fun cancelAllWork() {
        workManager.cancelUniqueWork(DataCollectionWorker.WORK_NAME)
        workManager.cancelUniqueWork("${AnalysisWorker.WORK_NAME}_daily")
        workManager.cancelUniqueWork("${AnalysisWorker.WORK_NAME}_weekly")
    }
    
    /**
     * Check if data collection is currently scheduled
     */
    fun isDataCollectionScheduled(): Boolean {
        val workInfos = workManager.getWorkInfosForUniqueWork(DataCollectionWorker.WORK_NAME).get()
        return workInfos.any { !it.state.isFinished }
    }
    
    private fun calculateDelayUntilMidnight(): Long {
        val now = System.currentTimeMillis()
        val midnight = java.util.Calendar.getInstance().apply {
            timeInMillis = now
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
            add(java.util.Calendar.DAY_OF_YEAR, 1)
        }.timeInMillis
        return midnight - now
    }
}
