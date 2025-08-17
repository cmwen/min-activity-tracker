package io.cmwen.min_activity_tracker

import android.app.Application
import io.cmwen.min_activity_tracker.di.AppContainer

class MinActivityApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
