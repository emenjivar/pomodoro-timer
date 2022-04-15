package com.emenjivar.pomodoro.di

import com.emenjivar.core.usecase.GetPomodoroUseCase
import com.emenjivar.core.usecase.IsNightModeUseCase
import com.emenjivar.core.usecase.SetNighModeUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import com.emenjivar.core.usecase.SetWorkTimeUseCase
import org.koin.dsl.module

val coreModule = module {
    single { GetPomodoroUseCase(get()) }
    single { SetWorkTimeUseCase(get()) }
    single { SetRestTimeUseCase(get()) }
    single { SetNighModeUseCase(get()) }
    single { IsNightModeUseCase(get()) }
}
