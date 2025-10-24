package io.cmwen.min_activity_tracker.features.tracking

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionClient
import dagger.hilt.android.AndroidEntryPoint
import io.cmwen.min_activity_tracker.R
import io.cmwen.min_activity_tracker.data.preferences.UserPreferences
import io.cmwen.min_activity_tracker.data.preferences.UserPreferencesRepository
import io.cmwen.min_activity_tracker.features.permissions.PermissionChecker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : Service() {
    @Inject
    lateinit var dataCollector: DataCollector

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    @Inject
    lateinit var permissionChecker: PermissionChecker

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private var dataCollectionJob: Job? = null
    private var activityRecognitionClient: ActivityRecognitionClient? = null
    private var activityUpdatesRegistered = false
    private var activityPendingIntent: PendingIntent? = null

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "TrackingServiceChannel"
        const val NOTIFICATION_ID = 1
        private const val COLLECTION_INTERVAL_MS = 60_000L
        private const val ACTIVITY_UPDATE_INTERVAL_MS = 60_000L
        private const val ACTIVITY_PENDING_INTENT_REQUEST_CODE = 42
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        createNotificationChannel()
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        scope.launch {
            userPreferencesRepository
                .preferencesFlow
                .collectLatest { preferences ->
                    restartDataCollection(preferences)
                    updateActivityRecognition(preferences)
                }
        }

        return START_STICKY
    }

    private fun restartDataCollection(preferences: UserPreferences) {
        dataCollectionJob?.cancel()
        if (!preferences.collectAppUsage && !preferences.collectBattery) {
            return
        }

        dataCollectionJob =
            scope.launch {
                while (isActive) {
                    val end = System.currentTimeMillis()
                    val start = end - COLLECTION_INTERVAL_MS
                    dataCollector.collectUsageData(start, end, preferences)
                    dataCollector.collectBatteryData(preferences)
                    delay(COLLECTION_INTERVAL_MS)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        stopActivityRecognitionUpdates()
        dataCollectionJob?.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Tracking Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT,
                )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification =
        NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Activity Tracker")
            .setContentText("Tracking your activity...")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()

    private fun updateActivityRecognition(preferences: UserPreferences) {
        if (preferences.collectActivityRecognition && permissionChecker.hasActivityRecognitionPermission()) {
            startActivityRecognitionUpdates()
        } else {
            stopActivityRecognitionUpdates()
        }
    }

    private fun startActivityRecognitionUpdates() {
        if (activityUpdatesRegistered) return

        if (activityRecognitionClient == null) {
            activityRecognitionClient = ActivityRecognition.getClient(this)
        }

        val pendingIntent = getActivityPendingIntent()
        activityRecognitionClient
            ?.requestActivityUpdates(ACTIVITY_UPDATE_INTERVAL_MS, pendingIntent)
            ?.addOnSuccessListener {
                activityUpdatesRegistered = true
                activityPendingIntent = pendingIntent
            }
            ?.addOnFailureListener {
                activityUpdatesRegistered = false
            }
    }

    private fun stopActivityRecognitionUpdates() {
        if (!activityUpdatesRegistered) return
        val client = activityRecognitionClient ?: return
        val pendingIntent = activityPendingIntent ?: return

        client
            .removeActivityUpdates(pendingIntent)
            .addOnCompleteListener {
                activityUpdatesRegistered = false
            }
    }

    private fun getActivityPendingIntent(): PendingIntent {
        val flags =
            PendingIntent.FLAG_UPDATE_CURRENT or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        val intent = Intent(this, ActivityRecognitionReceiver::class.java)
        return PendingIntent.getBroadcast(
            this,
            ACTIVITY_PENDING_INTENT_REQUEST_CODE,
            intent,
            flags,
        )
    }
}
