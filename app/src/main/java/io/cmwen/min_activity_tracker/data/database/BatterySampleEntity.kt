package io.cmwen.min_activity_tracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battery_samples")
data class BatterySampleEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val levelPercent: Int,
    val chargingState: String,
    val temperature: Float? = null,
)
