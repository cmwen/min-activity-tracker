package io.cmwen.min_activity_tracker.domain.usecase

import io.cmwen.min_activity_tracker.data.database.entities.BatterySampleEntity
import io.cmwen.min_activity_tracker.domain.repository.BatteryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBatteryHistoryUseCase @Inject constructor(
    private val repository: BatteryRepository
) {
    operator fun invoke(): Flow<List<BatterySampleEntity>> {
        return repository.getAllSamples()
    }
}
