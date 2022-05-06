package com.emenjivar.pomodoro.screens.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.closeSettings.observe(this, observeCloseSettings)
        viewModel.selectedColor.observe(this, observeColorSettings)

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

    private val observeColorSettings = Observer<Int?> { color ->
        color?.let { safeColor ->
            window.statusBarColor = ContextCompat.getColor(this, safeColor)
        }
    }
}
