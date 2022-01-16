package com.emenjivar.pomodoro

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.emenjivar.pomodoro.screens.countdown.CountDownScreen
import com.emenjivar.pomodoro.screens.countdown.CountDownViewModel
import com.emenjivar.pomodoro.screens.settings.SettingsActivity
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme

class MainActivity : ComponentActivity() {

    private lateinit var countDownViewModel: CountDownViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        countDownViewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)
        countDownViewModel.openSettings.observe(this, observeOpenSettings)

        setContent {
            PomodoroSchedulerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    CountDownScreen(
                        countDownViewModel = countDownViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private val observeOpenSettings = Observer<Boolean> { status ->
        if (status) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
