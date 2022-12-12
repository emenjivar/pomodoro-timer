package com.emenjivar.pomodoro.di

import com.emenjivar.pomodoro.usecases.AreSoundsEnableUseCase
import com.emenjivar.pomodoro.usecases.GetAutoPlayUseCase
import com.emenjivar.pomodoro.usecases.GetColorUseCase
import com.emenjivar.pomodoro.usecases.GetPomodoroUseCase
import com.emenjivar.pomodoro.usecases.IsKeepScreenOnUseCase
import com.emenjivar.pomodoro.usecases.IsNightModeUseCase
import com.emenjivar.pomodoro.usecases.IsVibrationEnabledUseCase
import com.emenjivar.pomodoro.usecases.SetAutoPlayUseCase
import com.emenjivar.pomodoro.usecases.SetColorUseCase
import com.emenjivar.pomodoro.usecases.SetKeepScreenOnUseCase
import com.emenjivar.pomodoro.usecases.SetNighModeUseCase
import com.emenjivar.pomodoro.usecases.SetRestTimeUseCase
import com.emenjivar.pomodoro.usecases.SetSoundsEnableUseCase
import com.emenjivar.pomodoro.usecases.SetVibrationUseCase
import com.emenjivar.pomodoro.usecases.SetWorkTimeUseCase
import org.koin.dsl.module

val coreModule = module {
    single { GetPomodoroUseCase(get()) }
    single { SetWorkTimeUseCase(get()) }
    single { SetRestTimeUseCase(get()) }
    single { GetAutoPlayUseCase(get()) }
    single { SetAutoPlayUseCase(get()) }
    single { SetNighModeUseCase(get()) }
    single { IsNightModeUseCase(get()) }
    single { IsKeepScreenOnUseCase(get()) }
    single { SetKeepScreenOnUseCase(get()) }
    single { IsVibrationEnabledUseCase(get()) }
    single { SetVibrationUseCase(get()) }
    single { AreSoundsEnableUseCase(get()) }
    single { SetSoundsEnableUseCase(get()) }
    single { GetColorUseCase(get()) }
    single { SetColorUseCase(get()) }
}
