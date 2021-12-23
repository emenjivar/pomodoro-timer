package com.emenjivar.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.emenjivar.pomodoro.countdown.CountDownLayout
import com.emenjivar.pomodoro.countdown.CountDownViewModel
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CountDownViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        viewModel.startTimer()

        setContent {
            PomodoroSchedulerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    CountDownLayout(
                        modifier = Modifier.fillMaxSize(),
                        time = "25:00",
                        progress = 0.98f,
                        pauseAction = {},
                        stopAction = {},
                        fullScreenAction = {}
                    )
                }
            }
        }
    }
}