package com.emenjivar.pomodoro.di

import com.emenjivar.pomodoro.screens.countdown.CountDownViewModel
import com.emenjivar.pomodoro.screens.settings.SettingsViewModel
import com.emenjivar.pomodoro.utils.MyNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        CountDownViewModel(
            getPomodoroTimeUseCase = get(),
            getRestTimeUseCase = get(),
            setNighModeUseCase = get(),
            isNightModeUseCase = get()
        )
    }
    viewModel {
        SettingsViewModel(
            getPomodoroTimeUseCase = get(),
            setPomodoroTimeUseCase = get(),
            getRestTimeUseCase = get(),
            setRestTimeUseCase = get()
        )
    }

    single {
        MyNotificationManager(androidContext())
    }
}
