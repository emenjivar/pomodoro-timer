package com.emenjivar.pomodoro.screens.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.Observer
import com.emenjivar.core.repository.SettingsRepository
import com.emenjivar.core.usecase.GetPomodoroTimeUseCase
import com.emenjivar.core.usecase.GetRestTimeUseCase
import com.emenjivar.core.usecase.SetPomodoroTimeUseCase
import com.emenjivar.core.usecase.SetRestTimeUseCase
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import com.emenjivar.data.repository.SettingsRepositoryImp

class SettingsActivity : ComponentActivity() {

    private lateinit var getPomodoroTimeUseCase: GetPomodoroTimeUseCase
    private lateinit var setPomodoroTimeUseCase: SetPomodoroTimeUseCase
    private lateinit var getRestTimeUseCase: GetRestTimeUseCase
    private lateinit var setRestTimeUseCase: SetRestTimeUseCase
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsRepository = SettingsRepositoryImp(applicationContext)
        getPomodoroTimeUseCase = GetPomodoroTimeUseCase(settingsRepository)
        setPomodoroTimeUseCase = SetPomodoroTimeUseCase(settingsRepository)

        getRestTimeUseCase = GetRestTimeUseCase(settingsRepository)
        setRestTimeUseCase = SetRestTimeUseCase(settingsRepository)

        viewModel = SettingsViewModel(
            getPomodoroTimeUseCase,
            setPomodoroTimeUseCase,
            getRestTimeUseCase,
            setRestTimeUseCase
        )

        viewModel.closeSettings.observe(this, observeCloseSettings)

        setContent {
            PomodoroSchedulerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SettingsScreen(viewModel)
                }
            }
        }
    }

    private val observeCloseSettings = Observer<Boolean> { status ->
        if (status)
            finish()
    }
}
