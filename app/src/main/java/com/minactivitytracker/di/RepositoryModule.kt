package com.minactivitytracker.di

import com.minactivitytracker.repository.ActivityRepository
import com.minactivitytracker.repository.BatteryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // Repositories are already annotated with @Singleton and @Inject, 
    // so explicit provision is not strictly necessary unless we have interfaces.
    // But keeping this module for future extensibility if we add interfaces.
}
