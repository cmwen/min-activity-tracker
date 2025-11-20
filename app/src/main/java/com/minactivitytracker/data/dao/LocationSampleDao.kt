package com.minactivitytracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.minactivitytracker.data.entity.LocationSample
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationSampleDao {
    @Insert
    suspend fun insert(location: LocationSample)

    @Query("SELECT * FROM location_samples ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentLocations(limit: Int = 100): Flow<List<LocationSample>>

    @Query("SELECT * FROM location_samples WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getLocationsInRange(startTime: Long, endTime: Long): Flow<List<LocationSample>>

    @Query("DELETE FROM location_samples WHERE timestamp < :before")
    suspend fun deleteOlderThan(before: Long)

    @Query("SELECT COUNT(*) FROM location_samples")
    suspend fun getCount(): Int
}
