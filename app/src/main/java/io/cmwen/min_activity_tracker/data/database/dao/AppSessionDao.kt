package io.cmwen.min_activity_tracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.cmwen.min_activity_tracker.data.database.entities.AppSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSessionDao {
    @Query("SELECT * FROM app_sessions ORDER BY startTimestamp DESC")
    fun getAllSessions(): Flow<List<AppSessionEntity>>

    @Query("SELECT * FROM app_sessions WHERE startTimestamp >= :startTime AND endTimestamp <= :endTime ORDER BY startTimestamp DESC")
    fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<AppSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: AppSessionEntity)

    @Query("DELETE FROM app_sessions")
    suspend fun clearAll()
}
