package com.minactivitytracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_samples")
data class LocationSample(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val provider: String
)
