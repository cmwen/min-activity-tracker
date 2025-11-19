package com.minactivitytracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.minactivitytracker.data.entity.AppSession
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSessionDao {
    @Insert
    suspend fun insert(session: AppSession)

    @Query("SELECT * FROM app_sessions ORDER BY startTimestamp DESC")
    fun getAllSessions(): Flow<List<AppSession>>

    @Query("SELECT * FROM app_sessions WHERE startTimestamp >= :startTime AND endTimestamp <= :endTime ORDER BY startTimestamp DESC")
    fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<AppSession>>
}
