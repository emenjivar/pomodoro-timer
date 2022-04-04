package com.emenjivar.pomodoro.di

import com.emenjivar.core.repository.SettingsRepository
import com.emenjivar.data.repository.SettingsRepositoryImp
import org.koin.dsl.module

val dataModule = module {
    single<SettingsRepository> { SettingsRepositoryImp(get()) }
}
