package io.cmwen.min_activity_tracker.features.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.cmwen.min_activity_tracker.data.database.AnalysisReportEntity
import io.cmwen.min_activity_tracker.data.repository.AnalysisReportRepository
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.UUID

/**
 * WorkManager worker for generating periodic analysis reports
 * Creates daily/weekly summaries and battery usage analysis
 */
@HiltWorker
class AnalysisWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val sessionRepository: SessionRepository,
        private val batterySampleRepository: BatterySampleRepository,
        private val analysisReportRepository: AnalysisReportRepository,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result =
            withContext(Dispatchers.IO) {
                try {
                    val reportType = inputData.getString(KEY_REPORT_TYPE) ?: "daily"
                    val daysBack = if (reportType == "weekly") 7 else 1

                    val endTime = System.currentTimeMillis()
                    val startTime = endTime - (daysBack * 24 * 60 * 60 * 1000L)

                    // Get sessions in time range
                    val sessions = sessionRepository.getSessionsInRange(startTime, endTime)

                    // Calculate metrics
                    val totalDuration = sessions.sumOf { it.durationMs }
                    val appUsageMap =
                        sessions
                            .groupBy { it.packageName }
                            .mapValues { (_, sessions) -> sessions.sumOf { it.durationMs } }
                            .entries
                            .sortedByDescending { it.value }
                            .take(10)
                            .map { it.key to it.value }

                    // Get battery samples
                    val batterySamples = batterySampleRepository.getSamplesInRange(startTime, endTime)
                    val batteryDrain =
                        if (batterySamples.isNotEmpty()) {
                            batterySamples.first().levelPercent - batterySamples.last().levelPercent
                        } else {
                            0
                        }

                    // Build metrics JSON
                    val metricsJson =
                        buildJsonObject {
                            put("totalDurationMs", totalDuration)
                            put("sessionCount", sessions.size)
                            put("uniqueApps", sessions.map { it.packageName }.distinct().size)
                            put("batteryDrain", batteryDrain)
                            put(
                                "topApps",
                                buildJsonObject {
                                    appUsageMap.forEach { pair ->
                                        put(pair.first, pair.second)
                                    }
                                },
                            )
                        }.toString()

                    // Create and save report
                    val report =
                        AnalysisReportEntity(
                            id = UUID.randomUUID().toString(),
                            rangeStartTs = startTime,
                            rangeEndTs = endTime,
                            createdTs = System.currentTimeMillis(),
                            reportType = reportType,
                            metricsJson = metricsJson,
                        )

                    analysisReportRepository.insert(report)

                    Result.success()
                } catch (e: Exception) {
                    if (runAttemptCount < MAX_RETRIES) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
            }

        companion object {
            private const val MAX_RETRIES = 3
            const val WORK_NAME = "analysis_work"
            const val KEY_REPORT_TYPE = "report_type"
        }
    }
