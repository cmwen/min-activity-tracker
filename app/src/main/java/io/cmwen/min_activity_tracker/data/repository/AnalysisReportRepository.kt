package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.AnalysisReportEntity
import kotlinx.coroutines.flow.Flow

interface AnalysisReportRepository {
    fun observeAllReports(): Flow<List<AnalysisReportEntity>>

    fun observeReportsByType(reportType: String): Flow<List<AnalysisReportEntity>>

    fun observeRecentReports(startTime: Long): Flow<List<AnalysisReportEntity>>

    suspend fun getLatestReport(): AnalysisReportEntity?

    suspend fun insert(report: AnalysisReportEntity)

    suspend fun deleteById(id: String)

    suspend fun deleteOldReports(beforeTimestamp: Long)
}
