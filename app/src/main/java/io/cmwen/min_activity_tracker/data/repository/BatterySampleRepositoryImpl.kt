package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.BatterySampleDao
import io.cmwen.min_activity_tracker.data.database.BatterySampleEntity
import kotlinx.coroutines.flow.Flow

class BatterySampleRepositoryImpl(
    private val dao: BatterySampleDao,
) : BatterySampleRepository {
    override fun observeAllSamples(): Flow<List<BatterySampleEntity>> = dao.getAllSamples()

    override fun observeSamplesByTimeRange(
        startTime: Long,
        endTime: Long,
    ): Flow<List<BatterySampleEntity>> = dao.getSamplesByTimeRange(startTime, endTime)

    override fun observeSamplesByChargingState(chargingState: String): Flow<List<BatterySampleEntity>> = dao.getSamplesByChargingState(chargingState)

    override fun observeRecentSamples(limit: Int): Flow<List<BatterySampleEntity>> = dao.getRecentSamples(limit)

    override suspend fun getSamplesInRange(
        startTime: Long,
        endTime: Long,
    ): List<BatterySampleEntity> = dao.getSamplesInRange(startTime, endTime)

    override suspend fun insert(sample: BatterySampleEntity) = dao.insert(sample)

    override suspend fun insertAll(samples: List<BatterySampleEntity>) = dao.insertAll(samples)

    override suspend fun deleteById(id: String) = dao.deleteById(id)

    override suspend fun deleteOldSamples(beforeTimestamp: Long) = dao.deleteOldSamples(beforeTimestamp)

    override suspend fun getAverageBatteryLevel(
        startTime: Long,
        endTime: Long,
    ): Double? = dao.getAverageBatteryLevel(startTime, endTime)
}
