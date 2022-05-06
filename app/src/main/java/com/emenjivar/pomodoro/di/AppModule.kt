package com.emenjivar.pomodoro.di

import com.emenjivar.pomodoro.screens.countdown.CountDownViewModel
import com.emenjivar.pomodoro.screens.settings.SettingsViewModel
import com.emenjivar.pomodoro.screens.splashscreen.SplashScreenViewModel
import com.emenjivar.pomodoro.system.CustomNotificationManager
import com.emenjivar.pomodoro.system.CustomNotificationManagerImp
import com.emenjivar.pomodoro.system.CustomVibrationImp
import com.emenjivar.pomodoro.system.CustomVibrator
import com.emenjivar.pomodoro.system.SoundManagerImp
import com.emenjivar.pomodoro.system.SoundsManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        SplashScreenViewModel(
            getColorUseCase = get()
        )
    }

    viewModel {
        CountDownViewModel(
            getColorUseCase = get(),
            getPomodoroUseCase = get(),
            setNighModeUseCase = get(),
            getAutoPlayUseCase = get(),
            isNightModeUseCase = get(),
            isKeepScreenOnUseCase = get(),
            isVibrationEnabledUseCase = get(),
            areSoundsEnableUseCase = get(),
            notificationManager = get(),
            customVibrator = get(),
            soundsManager = get()
        )
    }
    viewModel {
        SettingsViewModel(
            getColorUseCase = get(),
            setColorUseCase = get(),
            getPomodoroUseCase = get(),
            setWorkTimeUseCase = get(),
            setRestTimeUseCase = get(),
            getAutoPlayUseCase = get(),
            setAutoPlayUseCase = get(),
            isKeepScreenOnUseCase = get(),
            setKeepScreenOnUseCase = get(),
            isVibrationEnabledUseCase = get(),
            setVibrationUseCase = get(),
            areSoundsEnableUseCase = get(),
            setSoundsEnableUseCase = get(),
            customVibrator = get()
        )
    }

    single<CustomNotificationManager> {
        CustomNotificationManagerImp(androidContext())
    }

    single<CustomVibrator> {
        CustomVibrationImp(androidContext())
    }

    single<SoundsManager> {
        SoundManagerImp(androidContext())
    }
}
