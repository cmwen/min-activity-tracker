package io.cmwen.min_activity_tracker.features.tracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DeviceEventReceiver : BroadcastReceiver() {

    @Inject
    lateinit var deviceEventRepository: DeviceEventRepository

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        val timestamp = System.currentTimeMillis()
        val event = io.cmwen.min_activity_tracker.data.database.DeviceEventEntity(
            id = "device-${action}-${timestamp}",
            type = action,
            timestamp = timestamp
        )

        CoroutineScope(Dispatchers.IO).launch {
            deviceEventRepository.insert(event)
        }
    }
}
