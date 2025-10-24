package io.cmwen.min_activity_tracker.features.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.cmwen.min_activity_tracker.data.preferences.UserPreferencesRepository
import io.cmwen.min_activity_tracker.features.export.DataExporter
import io.cmwen.min_activity_tracker.features.export.ExportFormat
import io.cmwen.min_activity_tracker.features.export.ExportTimeRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@HiltWorker
class AutoExportWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val dataExporter: DataExporter,
        private val userPreferencesRepository: UserPreferencesRepository,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result =
            withContext(Dispatchers.IO) {
                val prefs = userPreferencesRepository.preferencesFlow.first()
                if (!prefs.autoExportEnabled) {
                    return@withContext Result.success()
                }

                val (start, end) = calculateRange(prefs.autoExportRange)
                val result =
                    when (prefs.autoExportFormat) {
                        ExportFormat.JSON -> dataExporter.exportToJson(start, end, prefs.autoExportAnonymize)
                        ExportFormat.CSV -> dataExporter.exportSessionsToCsv(start, end, prefs.autoExportAnonymize)
                    }

                return@withContext result.fold(
                    onSuccess = { Result.success() },
                    onFailure = { throwable ->
                        if (runAttemptCount < MAX_RETRIES) {
                            Result.retry()
                        } else {
                            Result.failure()
                        }
                    },
                )
            }

        private fun calculateRange(range: ExportTimeRange): Pair<Long, Long> {
            val end = System.currentTimeMillis()
            return when (range) {
                ExportTimeRange.ALL -> 0L to end
                ExportTimeRange.LAST_24_HOURS -> (end - ONE_DAY_MS) to end
            }
        }

        companion object {
            const val WORK_NAME = "auto_export_work"
            private const val ONE_DAY_MS = 24 * 60 * 60 * 1000L
            private const val MAX_RETRIES = 3
        }
    }
