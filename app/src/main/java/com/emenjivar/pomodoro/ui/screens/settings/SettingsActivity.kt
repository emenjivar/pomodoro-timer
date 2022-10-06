package com.emenjivar.pomodoro.ui.screens.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.compose.rememberNavController
import com.emenjivar.pomodoro.MainActivity
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : ComponentActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColor(getSelectedColor())
        setObservers()

        setContent {
            PomodoroSchedulerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SettingsScreen(
                        navController = rememberNavController(),
                        selectedColor = getSelectedColor()
                    )
                }
            }
        }
    }

    private fun getSelectedColor() = intent.extras?.getInt(MainActivity.EXTRA_SELECTED_COLOR) ?: R.color.primary

    private fun setStatusBarColor(selectedColor: Int?) {
        selectedColor?.let { safeColor ->
            if (safeColor != 0)
                window.statusBarColor = ContextCompat.getColor(this, safeColor)
        }
    }

    private fun setObservers() {
        viewModel.closeSettings.observe(this, observeCloseSettings)
        viewModel.selectedColor.observe(this, observeColorSettings)
    }

    private val observeCloseSettings = Observer<Boolean> { status ->
        if (status)
            finish()
    }

    private val observeColorSettings = Observer<Int?> { color ->
        setStatusBarColor(color)
    }
}
