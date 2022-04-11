package com.emenjivar.pomodoro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.sendBroadcast(
            Intent(INTENT_NAME)
                .putExtra(ACTION_NAME, intent.action)
        )
    }

    companion object {
        private const val TAG = "MyBroadcastReceiver"
        const val INTENT_NAME = "pomodoroCounter"
        const val ACTION_NAME = "pomodoroAction"
    }
}
