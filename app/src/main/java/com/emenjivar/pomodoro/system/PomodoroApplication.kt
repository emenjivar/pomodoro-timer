package com.emenjivar.pomodoro.system

import android.app.Application
import com.emenjivar.pomodoro.di.appModule
import com.emenjivar.pomodoro.di.coreModule
import com.emenjivar.pomodoro.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PomodoroApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PomodoroApplication)
            modules(appModule, coreModule, dataModule)
        }

        MyNotificationManager.createChannel(this)
    }
}
