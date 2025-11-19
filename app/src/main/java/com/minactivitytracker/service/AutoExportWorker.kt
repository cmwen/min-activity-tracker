package com.minactivitytracker.service

import android.content.Context
import android.os.Environment
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.minactivitytracker.repository.ActivityRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class AutoExportWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ActivityRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val sessions = repository.getAllSessions().first()
            val json = buildString {
                append("[")
                sessions.forEachIndexed { index, session ->
                    append("""{"package":"${session.packageName}","start":${session.startTimestamp},"end":${session.endTimestamp}}""")
                    if (index < sessions.size - 1) append(",")
                }
                append("]")
            }

            // Save to app-specific external storage
            val fileName = "activity_tracker_auto_export_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.json"
            val file = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            withContext(Dispatchers.IO) {
                FileWriter(file).use { writer ->
                    writer.write(json)
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
