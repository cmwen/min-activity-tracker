package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.BatterySampleEntity
import kotlinx.coroutines.flow.Flow

interface BatterySampleRepository {
    fun observeAllSamples(): Flow<List<BatterySampleEntity>>
    fun observeSamplesByTimeRange(startTime: Long, endTime: Long): Flow<List<BatterySampleEntity>>
    fun observeSamplesByChargingState(chargingState: String): Flow<List<BatterySampleEntity>>
    fun observeRecentSamples(limit: Int): Flow<List<BatterySampleEntity>>
    suspend fun insert(sample: BatterySampleEntity)
    suspend fun insertAll(samples: List<BatterySampleEntity>)
    suspend fun deleteById(id: String)
    suspend fun deleteOldSamples(beforeTimestamp: Long)
    suspend fun getAverageBatteryLevel(startTime: Long, endTime: Long): Double?
}
