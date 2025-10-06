package io.cmwen.min_activity_tracker.features.export

import android.content.Context
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Handles exporting collected data to various formats (JSON, CSV)
 */
class DataExporter @Inject constructor(
    private val context: Context,
    private val sessionRepository: SessionRepository,
    private val batterySampleRepository: BatterySampleRepository,
    private val deviceEventRepository: DeviceEventRepository
) {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }
    
    /**
     * Export all data to JSON format
     */
    suspend fun exportToJson(
        startTime: Long,
        endTime: Long,
        anonymize: Boolean = false
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val sessions = sessionRepository.getSessionsInRange(startTime, endTime)
            val batterySamples = batterySampleRepository.getSamplesInRange(startTime, endTime)
            val deviceEvents = deviceEventRepository.getEventsInRange(startTime, endTime)
            
            val exportData = ExportData(
                exportTimestamp = System.currentTimeMillis(),
                startTimestamp = startTime,
                endTimestamp = endTime,
                sessions = sessions.map { session ->
                    SessionExport(
                        id = session.id,
                        packageName = if (anonymize) anonymizePackageName(session.packageName) else session.packageName,
                        appLabel = if (anonymize) "App" else session.appLabel,
                        startTimestamp = session.startTimestamp,
                        endTimestamp = session.endTimestamp,
                        durationMs = session.durationMs,
                        startBatteryPct = session.startBatteryPct,
                        endBatteryPct = session.endBatteryPct,
                        locationLatitude = if (anonymize) null else session.locationLatitude,
                        locationLongitude = if (anonymize) null else session.locationLongitude
                    )
                },
                batterySamples = batterySamples.map { sample ->
                    BatterySampleExport(
                        id = sample.id,
                        timestamp = sample.timestamp,
                        levelPercent = sample.levelPercent,
                        chargingState = sample.chargingState,
                        temperature = sample.temperature
                    )
                },
                deviceEvents = deviceEvents.map { event ->
                    DeviceEventExport(
                        id = event.id,
                        type = event.type,
                        timestamp = event.timestamp
                    )
                }
            )
            
            val jsonString = json.encodeToString(exportData)
            val file = createExportFile("json")
            file.writeText(jsonString)
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Export sessions to CSV format
     */
    suspend fun exportSessionsToCsv(
        startTime: Long,
        endTime: Long,
        anonymize: Boolean = false
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val sessions = sessionRepository.getSessionsInRange(startTime, endTime)
            val csv = buildString {
                appendLine("id,packageName,appLabel,startTimestamp,endTimestamp,durationMs,startBatteryPct,endBatteryPct,locationLatitude,locationLongitude")
                sessions.forEach { session ->
                    appendLine(
                        "${session.id}," +
                        "${if (anonymize) anonymizePackageName(session.packageName) else session.packageName}," +
                        "\"${if (anonymize) "App" else session.appLabel}\"," +
                        "${session.startTimestamp}," +
                        "${session.endTimestamp}," +
                        "${session.durationMs}," +
                        "${session.startBatteryPct ?: ""}," +
                        "${session.endBatteryPct ?: ""}," +
                        "${if (anonymize) "" else (session.locationLatitude ?: "")}," +
                        "${if (anonymize) "" else (session.locationLongitude ?: "")}"
                    )
                }
            }
            
            val file = createExportFile("csv")
            file.writeText(csv)
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get the app's export directory
     */
    fun getExportDirectory(): File {
        val dir = File(context.getExternalFilesDir(null), "exports")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }
    
    private fun createExportFile(extension: String): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val filename = "activity_export_$timestamp.$extension"
        return File(getExportDirectory(), filename)
    }
    
    private fun anonymizePackageName(packageName: String): String {
        return "app.${packageName.hashCode().toString(16)}"
    }
}

@Serializable
data class ExportData(
    val exportTimestamp: Long,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val sessions: List<SessionExport>,
    val batterySamples: List<BatterySampleExport>,
    val deviceEvents: List<DeviceEventExport>
)

@Serializable
data class SessionExport(
    val id: String,
    val packageName: String,
    val appLabel: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val durationMs: Long,
    val startBatteryPct: Int?,
    val endBatteryPct: Int?,
    val locationLatitude: Double?,
    val locationLongitude: Double?
)

@Serializable
data class BatterySampleExport(
    val id: String,
    val timestamp: Long,
    val levelPercent: Int,
    val chargingState: String,
    val temperature: Float?
)

@Serializable
data class DeviceEventExport(
    val id: String,
    val type: String,
    val timestamp: Long
)
