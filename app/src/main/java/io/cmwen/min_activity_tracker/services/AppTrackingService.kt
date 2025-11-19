package io.cmwen.min_activity_tracker.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import io.cmwen.min_activity_tracker.data.database.entities.AppSessionEntity
import io.cmwen.min_activity_tracker.domain.repository.AppSessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class AppTrackingService : Service() {

    @Inject
    lateinit var repository: AppSessionRepository

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, createNotification())
        startTracking()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    private fun startTracking() {
        serviceScope.launch {
            val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            var lastCheckTime = System.currentTimeMillis() - 60 * 1000 // Start checking from 1 minute ago

            while (true) {
                val currentTime = System.currentTimeMillis()
                val events = usageStatsManager.queryEvents(lastCheckTime, currentTime)
                val event = UsageEvents.Event()
                
                while (events.hasNextEvent()) {
                    events.getNextEvent(event)
                    if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        // Simplified tracking: just log the start of an app
                        // In a real app, we would track duration by matching with MOVE_TO_BACKGROUND
                        val session = AppSessionEntity(
                            id = UUID.randomUUID().toString(),
                            packageName = event.packageName,
                            appLabel = event.packageName, // TODO: Get actual label
                            startTimestamp = event.timeStamp,
                            endTimestamp = event.timeStamp, // Placeholder
                            durationMs = 0, // Placeholder
                            startBatteryPct = null,
                            endBatteryPct = null,
                            locationLatitude = null,
                            locationLongitude = null,
                            metadataJson = null
                        )
                        repository.insertSession(session)
                    }
                }
                
                lastCheckTime = currentTime
                delay(60 * 1000) // Check every minute
            }
        }
    }

    private fun createNotification(): Notification {
        val channelId = "tracking_service_channel"
        val channelName = "App Tracking Service"
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Activity Tracker Running")
            .setContentText("Tracking app usage in background")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
    }
}
