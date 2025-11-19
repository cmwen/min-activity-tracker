package com.minactivitytracker.service

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.minactivitytracker.data.entity.BatterySample
import com.minactivitytracker.repository.BatteryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BatterySamplingWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: BatteryRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val batteryStatus: Intent? = applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        
        val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPct = if (level != -1 && scale != -1) {
            (level * 100 / scale.toFloat()).toInt()
        } else {
            -1
        }

        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        if (batteryPct != -1) {
            repository.insertSample(
                BatterySample(
                    timestamp = System.currentTimeMillis(),
                    levelPct = batteryPct,
                    isCharging = isCharging
                )
            )
        }

        return Result.success()
    }
}
