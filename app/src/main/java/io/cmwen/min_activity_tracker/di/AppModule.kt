package io.cmwen.min_activity_tracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.cmwen.min_activity_tracker.data.database.AppDatabase
import io.cmwen.min_activity_tracker.data.database.dao.AppSessionDao
import io.cmwen.min_activity_tracker.data.database.dao.BatteryDao
import io.cmwen.min_activity_tracker.data.database.dao.DeviceEventDao
import io.cmwen.min_activity_tracker.data.repository.AppSessionRepositoryImpl
import io.cmwen.min_activity_tracker.data.repository.BatteryRepositoryImpl
import io.cmwen.min_activity_tracker.domain.repository.AppSessionRepository
import io.cmwen.min_activity_tracker.domain.repository.BatteryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "min_activity_tracker.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAppSessionDao(database: AppDatabase): AppSessionDao = database.appSessionDao()

    @Provides
    @Singleton
    fun provideBatteryDao(database: AppDatabase): BatteryDao = database.batteryDao()

    @Provides
    @Singleton
    fun provideDeviceEventDao(database: AppDatabase): DeviceEventDao = database.deviceEventDao()

    @Provides
    @Singleton
    fun provideAppSessionRepository(impl: AppSessionRepositoryImpl): AppSessionRepository = impl

    @Provides
    @Singleton
    fun provideBatteryRepository(impl: BatteryRepositoryImpl): BatteryRepository = impl
}
