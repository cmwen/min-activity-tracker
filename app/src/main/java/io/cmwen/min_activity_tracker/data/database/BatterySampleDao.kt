package io.cmwen.min_activity_tracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BatterySampleDao {
    @Query("SELECT * FROM battery_samples ORDER BY timestamp DESC")
    fun getAllSamples(): Flow<List<BatterySampleEntity>>

    @Query("SELECT * FROM battery_samples WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getSamplesByTimeRange(
        startTime: Long,
        endTime: Long,
    ): Flow<List<BatterySampleEntity>>

    @Query("SELECT * FROM battery_samples WHERE chargingState = :chargingState ORDER BY timestamp DESC")
    fun getSamplesByChargingState(chargingState: String): Flow<List<BatterySampleEntity>>

    @Query("SELECT * FROM battery_samples ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSamples(limit: Int): Flow<List<BatterySampleEntity>>

    @Query("SELECT * FROM battery_samples WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    suspend fun getSamplesInRange(
        startTime: Long,
        endTime: Long,
    ): List<BatterySampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sample: BatterySampleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(samples: List<BatterySampleEntity>)

    @Query("DELETE FROM battery_samples WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM battery_samples WHERE timestamp < :beforeTimestamp")
    suspend fun deleteOldSamples(beforeTimestamp: Long)

    @Query("SELECT AVG(levelPercent) FROM battery_samples WHERE timestamp BETWEEN :startTime AND :endTime")
    suspend fun getAverageBatteryLevel(
        startTime: Long,
        endTime: Long,
    ): Double?
}
