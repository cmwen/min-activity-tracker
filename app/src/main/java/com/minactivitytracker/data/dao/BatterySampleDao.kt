package com.minactivitytracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.minactivitytracker.data.entity.BatterySample
import kotlinx.coroutines.flow.Flow

@Dao
interface BatterySampleDao {
    @Insert
    suspend fun insert(sample: BatterySample)

    @Query("SELECT * FROM battery_samples ORDER BY timestamp DESC")
    fun getAllSamples(): Flow<List<BatterySample>>
}
