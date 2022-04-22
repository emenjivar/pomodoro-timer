package com.emenjivar.pomodoro.di

import com.emenjivar.pomodoro.screens.countdown.CountDownViewModel
import com.emenjivar.pomodoro.screens.settings.SettingsViewModel
import com.emenjivar.pomodoro.system.CustomNotificationManager
import com.emenjivar.pomodoro.system.CustomNotificationManagerImp
import com.emenjivar.pomodoro.system.CustomVibrationImp
import com.emenjivar.pomodoro.system.CustomVibrator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        CountDownViewModel(
            getPomodoroUseCase = get(),
            setNighModeUseCase = get(),
            getAutoPlayUseCase = get(),
            isNightModeUseCase = get(),
            isKeepScreenOnUseCase = get(),
            isVibrationEnabledUseCase = get(),
            notificationManager = get(),
            customVibrator = get()
        )
    }
    viewModel {
        SettingsViewModel(
            getPomodoroUseCase = get(),
            setWorkTimeUseCase = get(),
            setRestTimeUseCase = get(),
            getAutoPlayUseCase = get(),
            setAutoPlayUseCase = get(),
            isKeepScreenOnUseCase = get(),
            setKeepScreenOnUseCase = get(),
            isVibrationEnabledUseCase = get(),
            setVibrationUseCase = get()
        )
    }

    single<CustomNotificationManager> {
        CustomNotificationManagerImp(androidContext())
    }

    single<CustomVibrator> {
        CustomVibrationImp(androidContext())
    }
}
