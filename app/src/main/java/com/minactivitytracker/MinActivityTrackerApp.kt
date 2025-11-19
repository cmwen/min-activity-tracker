package com.minactivitytracker

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MinActivityTrackerApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleWorkers()
    }

    private fun scheduleWorkers() {
        val workManager = androidx.work.WorkManager.getInstance(this)

        val usageRequest = androidx.work.PeriodicWorkRequestBuilder<com.minactivitytracker.service.UsageTrackingWorker>(
            15, java.util.concurrent.TimeUnit.MINUTES
        ).build()

        val batteryRequest = androidx.work.PeriodicWorkRequestBuilder<com.minactivitytracker.service.BatterySamplingWorker>(
            15, java.util.concurrent.TimeUnit.MINUTES
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "usage_tracking",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            usageRequest
        )

        workManager.enqueueUniquePeriodicWork(
            "battery_sampling",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            batteryRequest
        )
    }
}
