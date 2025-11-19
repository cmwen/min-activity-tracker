package com.minactivitytracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_events")
data class DeviceEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // SCREEN_ON, SCREEN_OFF, CHARGING_STARTED, CHARGING_STOPPED
    val timestamp: Long
)
