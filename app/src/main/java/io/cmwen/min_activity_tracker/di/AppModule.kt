package io.cmwen.min_activity_tracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.cmwen.min_activity_tracker.core.error.ErrorHandler
import io.cmwen.min_activity_tracker.data.database.AnalysisReportDao
import io.cmwen.min_activity_tracker.data.database.BatterySampleDao
import io.cmwen.min_activity_tracker.data.database.DatabaseProvider
import io.cmwen.min_activity_tracker.data.database.DeviceEventDao
import io.cmwen.min_activity_tracker.data.database.MinActivityDatabase
import io.cmwen.min_activity_tracker.data.database.SessionDao
import io.cmwen.min_activity_tracker.data.preferences.UserPreferencesRepository
import io.cmwen.min_activity_tracker.data.repository.AnalysisReportRepository
import io.cmwen.min_activity_tracker.data.repository.AnalysisReportRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepositoryImpl
import javax.inject.Singleton

/**
 * Hilt dependency injection module for the application
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val USER_PREFERENCES_FILE = "user_preferences"

    @Provides
    @Singleton
    fun provideErrorHandler(): ErrorHandler = ErrorHandler.getInstance()

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): MinActivityDatabase = DatabaseProvider.getDatabase(context)

    @Provides
    fun provideSessionDao(database: MinActivityDatabase): SessionDao = database.sessionDao()

    @Provides
    fun provideDeviceEventDao(database: MinActivityDatabase): DeviceEventDao = database.deviceEventDao()

    @Provides
    fun provideBatterySampleDao(database: MinActivityDatabase): BatterySampleDao = database.batterySampleDao()

    @Provides
    fun provideAnalysisReportDao(database: MinActivityDatabase): AnalysisReportDao = database.analysisReportDao()

    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES_FILE) },
        )

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataStore: DataStore<Preferences>,
    ): UserPreferencesRepository = UserPreferencesRepository(dataStore)

    @Provides
    @Singleton
    fun provideSessionRepository(sessionDao: SessionDao): SessionRepository = SessionRepositoryImpl(sessionDao)

    @Provides
    @Singleton
    fun provideDeviceEventRepository(deviceEventDao: DeviceEventDao): DeviceEventRepository = DeviceEventRepositoryImpl(deviceEventDao)

    @Provides
    @Singleton
    fun provideBatterySampleRepository(batterySampleDao: BatterySampleDao): BatterySampleRepository = BatterySampleRepositoryImpl(batterySampleDao)

    @Provides
    @Singleton
    fun provideAnalysisReportRepository(analysisReportDao: AnalysisReportDao): AnalysisReportRepository = AnalysisReportRepositoryImpl(analysisReportDao)
}
