package io.cmwen.min_activity_tracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Query("SELECT * FROM app_sessions ORDER BY startTimestamp DESC")
    fun getAllSessions(): Flow<List<AppSessionEntity>>

    @Query("SELECT * FROM app_sessions WHERE startTimestamp >= :startTime AND endTimestamp <= :endTime ORDER BY startTimestamp DESC")
    suspend fun getSessionsInRange(startTime: Long, endTime: Long): List<AppSessionEntity>

    @Query("SELECT * FROM app_sessions WHERE id = :id")
    suspend fun getSessionById(id: String): AppSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: AppSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sessions: List<AppSessionEntity>)

    @Query("DELETE FROM app_sessions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM app_sessions WHERE endTimestamp < :timestamp")
    suspend fun deleteSessionsOlderThan(timestamp: Long)
}
