package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.dao.BatteryDao
import io.cmwen.min_activity_tracker.data.database.entities.BatterySampleEntity
import io.cmwen.min_activity_tracker.domain.repository.BatteryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(
    private val dao: BatteryDao
) : BatteryRepository {
    override fun getAllSamples(): Flow<List<BatterySampleEntity>> = dao.getAllSamples()

    override suspend fun insertSample(sample: BatterySampleEntity) = dao.insertSample(sample)
}
