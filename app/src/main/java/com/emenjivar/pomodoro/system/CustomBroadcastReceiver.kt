package com.emenjivar.pomodoro.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CustomBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.sendBroadcast(
            Intent(INTENT_NAME)
                .putExtra(ACTION_NAME, intent.action)
        )
    }

    companion object {
        const val INTENT_NAME = "pomodoroCounter"
        const val ACTION_NAME = "pomodoroAction"
    }
}
