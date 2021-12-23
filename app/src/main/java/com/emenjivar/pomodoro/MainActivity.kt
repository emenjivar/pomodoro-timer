package com.emenjivar.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.emenjivar.pomodoro.countdown.CountDownScreen
import com.emenjivar.pomodoro.countdown.CountDownViewModel
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CountDownViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CountDownViewModel::class.java)

        setContent {
            PomodoroSchedulerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    CountDownScreen(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxSize(),
                        pauseAction = { viewModel.startTimer() },
                        stopAction = {},
                        fullScreenAction = {}
                    )
                }
            }
        }
    }
}