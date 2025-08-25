package io.cmwen.min_activity_tracker.di

import android.content.Context
import io.cmwen.min_activity_tracker.core.error.ErrorHandler
import io.cmwen.min_activity_tracker.data.database.DatabaseProvider
import io.cmwen.min_activity_tracker.data.database.MinActivityDatabase
import io.cmwen.min_activity_tracker.data.repository.AnalysisReportRepository
import io.cmwen.min_activity_tracker.data.repository.AnalysisReportRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepositoryImpl

/**
 * Manual dependency injection container.
 * Used instead of Hilt due to kapt compatibility issues with current toolchain.
 */
class AppContainer(
    private val context: Context,
) {
    // Error handling
    val errorHandler: ErrorHandler by lazy {
        ErrorHandler.getInstance()
    }

    // Database
    private val database: MinActivityDatabase by lazy {
        DatabaseProvider.getDatabase(context)
    }

    // DAOs
    private val sessionDao by lazy { database.sessionDao() }
    private val deviceEventDao by lazy { database.deviceEventDao() }
    private val batterySampleDao by lazy { database.batterySampleDao() }
    private val analysisReportDao by lazy { database.analysisReportDao() }

    // Repositories
    val sessionRepository: SessionRepository by lazy {
        SessionRepositoryImpl(sessionDao)
    }

    val deviceEventRepository: DeviceEventRepository by lazy {
        DeviceEventRepositoryImpl(deviceEventDao)
    }

    val batterySampleRepository: BatterySampleRepository by lazy {
        BatterySampleRepositoryImpl(batterySampleDao)
    }

    val analysisReportRepository: AnalysisReportRepository by lazy {
        AnalysisReportRepositoryImpl(analysisReportDao)
    }
}
