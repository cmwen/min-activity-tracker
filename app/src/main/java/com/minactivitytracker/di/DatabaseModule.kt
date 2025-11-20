package com.minactivitytracker.di

import android.content.Context
import androidx.room.Room
import com.minactivitytracker.data.AppDatabase
import com.minactivitytracker.data.dao.AppSessionDao
import com.minactivitytracker.data.dao.BatterySampleDao
import com.minactivitytracker.data.dao.DeviceEventDao
import com.minactivitytracker.data.dao.LocationSampleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "min_activity_tracker.db"
        ).fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideAppSessionDao(database: AppDatabase): AppSessionDao = database.appSessionDao()

    @Provides
    fun provideDeviceEventDao(database: AppDatabase): DeviceEventDao = database.deviceEventDao()

    @Provides
    fun provideBatterySampleDao(database: AppDatabase): BatterySampleDao = database.batterySampleDao()

    @Provides
    fun provideLocationSampleDao(database: AppDatabase): LocationSampleDao = database.locationSampleDao()
}
