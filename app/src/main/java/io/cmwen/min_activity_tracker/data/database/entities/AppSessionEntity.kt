package io.cmwen.min_activity_tracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_sessions")
data class AppSessionEntity(
    @PrimaryKey val id: String,
    val packageName: String,
    val appLabel: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val durationMs: Long,
    val startBatteryPct: Int?,
    val endBatteryPct: Int?,
    val locationLatitude: Double?,
    val locationLongitude: Double?,
    val metadataJson: String?
)
