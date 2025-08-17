package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.AnalysisReportDao
import io.cmwen.min_activity_tracker.data.database.AnalysisReportEntity
import kotlinx.coroutines.flow.Flow

class AnalysisReportRepositoryImpl(
    private val dao: AnalysisReportDao
) : AnalysisReportRepository {
    override fun observeAllReports(): Flow<List<AnalysisReportEntity>> = dao.getAllReports()
    
    override fun observeReportsByType(reportType: String): Flow<List<AnalysisReportEntity>> = 
        dao.getReportsByType(reportType)
    
    override fun observeRecentReports(startTime: Long): Flow<List<AnalysisReportEntity>> = 
        dao.getRecentReports(startTime)
    
    override suspend fun getLatestReport(): AnalysisReportEntity? = dao.getLatestReport()
    
    override suspend fun insert(report: AnalysisReportEntity) = dao.insert(report)
    
    override suspend fun deleteById(id: String) = dao.deleteById(id)
    
    override suspend fun deleteOldReports(beforeTimestamp: Long) = dao.deleteOldReports(beforeTimestamp)
}