package com.emenjivar.pomodoro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import com.emenjivar.pomodoro.screens.countdown.CountDownScreen
import com.emenjivar.pomodoro.screens.countdown.CountDownViewModel
import com.emenjivar.pomodoro.screens.settings.SettingsActivity
import com.emenjivar.pomodoro.system.CustomBroadcastReceiver
import com.emenjivar.pomodoro.system.CustomNotificationManagerImp.Companion.INTENT_PAUSE
import com.emenjivar.pomodoro.system.CustomNotificationManagerImp.Companion.INTENT_PLAY
import com.emenjivar.pomodoro.system.CustomNotificationManagerImp.Companion.INTENT_STOP
import com.emenjivar.pomodoro.ui.theme.PomodoroSchedulerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val countDownViewModel: CountDownViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerReceiver(broadcastReceiver, IntentFilter(CustomBroadcastReceiver.INTENT_NAME))
        countDownViewModel.openSettings.observe(this, observeOpenSettings)
        countDownViewModel.keepScreenOn.observe(this, observeKeepScreenOn)

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

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onRestart() {
        super.onRestart()
        with(countDownViewModel) {
            closeNotification()

            // Make sure to call this properties on every onRestart
            forceFetchKeepScreenConfig()
            forceFetchVibrationConfig()
            forceFetchSoundsConfig()
        }
    }

    override fun onStop() {
        super.onStop()
        /**
         * just set displayNotification flag on true
         * this viewModel will display the notification
         * when the countdown is updated
         */
        countDownViewModel.displayNotification = true
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownViewModel.closeNotification()
        unregisterReceiver(broadcastReceiver)
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.extras?.getString(CustomBroadcastReceiver.ACTION_NAME)) {
                INTENT_PLAY -> {
                    countDownViewModel.resumeCounter()
                }
                INTENT_PAUSE -> {
                    countDownViewModel.pauseCounter()
                }
                INTENT_STOP -> {
                    countDownViewModel.stopCounter()
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

    private val observeKeepScreenOn = Observer<Boolean?> { flag ->
        // Verify null to avoid performing an action before value is set on viewModel
        flag?.let { safeFlag ->
            if (safeFlag) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }
}
