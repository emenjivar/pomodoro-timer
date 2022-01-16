package com.emenjivar.pomodoro.screens.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme

class SettingsActivity : ComponentActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

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
