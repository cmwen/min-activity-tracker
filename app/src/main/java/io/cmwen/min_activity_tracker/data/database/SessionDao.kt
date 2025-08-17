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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: AppSessionEntity)

    @Query("DELETE FROM app_sessions WHERE id = :id")
    suspend fun deleteById(id: String)
}
