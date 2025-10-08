package io.cmwen.min_activity_tracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.cmwen.min_activity_tracker.features.permissions.PermissionManager
import io.mockk.mockk
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Singleton
    fun providePermissionManager(): PermissionManager = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideSessionRepository(): SessionRepository = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideDeviceEventRepository(): DeviceEventRepository = mockk(relaxed = true)

    @Provides
    @Singleton
    fun provideBatterySampleRepository(): BatterySampleRepository = mockk(relaxed = true)
}
