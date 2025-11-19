package io.cmwen.min_activity_tracker.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_events")
data class DeviceEventEntity(
    @PrimaryKey val id: String,
    val type: String, // Storing enum as String
    val timestamp: Long,
    val detailsJson: String?
)
