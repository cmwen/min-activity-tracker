package io.cmwen.min_activity_tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.cmwen.min_activity_tracker.data.database.entities.BatterySampleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BatteryDao {
    @Query("SELECT * FROM battery_samples ORDER BY timestamp DESC")
    fun getAllSamples(): Flow<List<BatterySampleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSample(sample: BatterySampleEntity)
}
