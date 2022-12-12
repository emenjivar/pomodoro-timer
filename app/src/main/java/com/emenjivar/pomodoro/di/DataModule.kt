package com.emenjivar.pomodoro.di

import com.emenjivar.pomodoro.data.SettingsRepository
import com.emenjivar.pomodoro.data.SettingsRepositoryImp
import com.emenjivar.pomodoro.data.SharedSettingsRepository
import com.emenjivar.pomodoro.data.SharedSettingsRepositoryImp
import org.koin.dsl.module

val dataModule = module {
    single<SettingsRepository> { SettingsRepositoryImp(get()) }
    single<SharedSettingsRepository> { SharedSettingsRepositoryImp() }
}
