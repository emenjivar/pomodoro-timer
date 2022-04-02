package com.emenjivar.pomodoro.di

import com.emenjivar.core.usecase.GetPomodoroTimeUseCase
import com.emenjivar.core.usecase.GetRestTimeUseCase
import com.emenjivar.core.usecase.SetPomodoroTimeUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import org.koin.dsl.module

val coreModule = module {
    single { GetPomodoroTimeUseCase(get()) }
    single { SetPomodoroTimeUseCase(get()) }
    single { GetRestTimeUseCase(get()) }
    single { SetRestTimeUseCase(get()) }
}
