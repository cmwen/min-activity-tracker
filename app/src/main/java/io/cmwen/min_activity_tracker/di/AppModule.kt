package io.cmwen.min_activity_tracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.cmwen.min_activity_tracker.core.error.ErrorHandler
import io.cmwen.min_activity_tracker.data.database.*
import io.cmwen.min_activity_tracker.data.repository.*
import javax.inject.Singleton

/**
 * Hilt dependency injection module for the application
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler {
        return ErrorHandler.getInstance()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MinActivityDatabase {
        return DatabaseProvider.getDatabase(context)
    }

    @Provides
    fun provideSessionDao(database: MinActivityDatabase): SessionDao {
        return database.sessionDao()
    }

    @Provides
    fun provideDeviceEventDao(database: MinActivityDatabase): DeviceEventDao {
        return database.deviceEventDao()
    }

    @Provides
    fun provideBatterySampleDao(database: MinActivityDatabase): BatterySampleDao {
        return database.batterySampleDao()
    }

    @Provides
    fun provideAnalysisReportDao(database: MinActivityDatabase): AnalysisReportDao {
        return database.analysisReportDao()
    }

    @Provides
    @Singleton
    fun provideSessionRepository(sessionDao: SessionDao): SessionRepository {
        return SessionRepositoryImpl(sessionDao)
    }

    @Provides
    @Singleton
    fun provideDeviceEventRepository(deviceEventDao: DeviceEventDao): DeviceEventRepository {
        return DeviceEventRepositoryImpl(deviceEventDao)
    }

    @Provides
    @Singleton
    fun provideBatterySampleRepository(batterySampleDao: BatterySampleDao): BatterySampleRepository {
        return BatterySampleRepositoryImpl(batterySampleDao)
    }

    @Provides
    @Singleton
    fun provideAnalysisReportRepository(analysisReportDao: AnalysisReportDao): AnalysisReportRepository {
        return AnalysisReportRepositoryImpl(analysisReportDao)
    }
}
