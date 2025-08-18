package io.cmwen.min_activity_tracker.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisReportDao {
    @Query("SELECT * FROM analysis_reports ORDER BY createdTs DESC")
    fun getAllReports(): Flow<List<AnalysisReportEntity>>

    @Query("SELECT * FROM analysis_reports WHERE reportType = :reportType ORDER BY createdTs DESC")
    fun getReportsByType(reportType: String): Flow<List<AnalysisReportEntity>>

    @Query("SELECT * FROM analysis_reports WHERE rangeStartTs >= :startTime ORDER BY createdTs DESC")
    fun getRecentReports(startTime: Long): Flow<List<AnalysisReportEntity>>

    @Query("SELECT * FROM analysis_reports ORDER BY createdTs DESC LIMIT 1")
    suspend fun getLatestReport(): AnalysisReportEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: AnalysisReportEntity)

    @Query("DELETE FROM analysis_reports WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM analysis_reports WHERE createdTs < :beforeTimestamp")
    suspend fun deleteOldReports(beforeTimestamp: Long)
}
