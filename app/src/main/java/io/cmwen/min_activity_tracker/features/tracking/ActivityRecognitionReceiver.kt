package io.cmwen.min_activity_tracker.features.tracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import dagger.hilt.android.AndroidEntryPoint
import io.cmwen.min_activity_tracker.data.database.DeviceEventEntity
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@AndroidEntryPoint
class ActivityRecognitionReceiver : BroadcastReceiver() {
    @Inject
    lateinit var deviceEventRepository: DeviceEventRepository

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val result = ActivityRecognitionResult.extractResult(intent) ?: return
        val mostProbable = result.mostProbableActivity ?: return

        val timestamp = System.currentTimeMillis()
        val activityLabel = mapActivityType(mostProbable.type)
        val detailsJson =
            buildJsonObject {
                put("confidence", mostProbable.confidence)
                put(
                    "activities",
                    buildJsonObject {
                        result.probableActivities.forEach { detectedActivity ->
                            put(mapActivityType(detectedActivity.type), detectedActivity.confidence)
                        }
                    },
                )
            }.toString()

        val event =
            DeviceEventEntity(
                id = "activity-$timestamp",
                type = "ACTIVITY_$activityLabel",
                timestamp = timestamp,
                detailsJson = detailsJson,
            )

        CoroutineScope(Dispatchers.IO).launch {
            deviceEventRepository.insert(event)
        }
    }

    private fun mapActivityType(type: Int): String =
        when (type) {
            DetectedActivity.IN_VEHICLE -> "IN_VEHICLE"
            DetectedActivity.ON_BICYCLE -> "ON_BICYCLE"
            DetectedActivity.ON_FOOT -> "ON_FOOT"
            DetectedActivity.RUNNING -> "RUNNING"
            DetectedActivity.STILL -> "STILL"
            DetectedActivity.TILTING -> "TILTING"
            DetectedActivity.WALKING -> "WALKING"
            DetectedActivity.UNKNOWN -> "UNKNOWN"
            else -> "UNCLASSIFIED"
        }
}
