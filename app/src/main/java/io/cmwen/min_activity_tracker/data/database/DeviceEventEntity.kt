package io.cmwen.min_activity_tracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_events")
data class DeviceEventEntity(
    @PrimaryKey val id: String,
    val type: String,
    val timestamp: Long,
)
