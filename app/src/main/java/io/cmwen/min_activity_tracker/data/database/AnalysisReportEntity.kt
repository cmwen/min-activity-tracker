package io.cmwen.min_activity_tracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_reports")
data class AnalysisReportEntity(
    @PrimaryKey val id: String,
    val rangeStartTs: Long,
    val rangeEndTs: Long,
    val createdTs: Long,
    val reportType: String,
    val metricsJson: String
)
