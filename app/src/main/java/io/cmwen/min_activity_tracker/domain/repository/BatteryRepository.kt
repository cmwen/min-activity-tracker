package io.cmwen.min_activity_tracker.domain.repository

import io.cmwen.min_activity_tracker.data.database.entities.BatterySampleEntity
import kotlinx.coroutines.flow.Flow

interface BatteryRepository {
    fun getAllSamples(): Flow<List<BatterySampleEntity>>
    suspend fun insertSample(sample: BatterySampleEntity)
}
