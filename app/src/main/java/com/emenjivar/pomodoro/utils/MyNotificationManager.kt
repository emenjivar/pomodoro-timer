package com.emenjivar.pomodoro.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.emenjivar.pomodoro.MyBroadcastReceiver
import com.emenjivar.pomodoro.R

class MyNotificationManager(private val context: Context) {

    lateinit var builder: NotificationCompat.Builder

    init {
        buildNotification()
    }

    private fun buildNotification(action: Action = Action.Play) {
        builder = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(ICON)
            .setContentTitle("Pomodoro")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        when (action) {
            is Action.Play -> {
                builder.setContentText("Time - running")
                builder.addAction(R.drawable.ic_baseline_pause_24, "PAUSE", pauseIntent())
                builder.addAction(R.drawable.ic_baseline_stop_24, "STOP", stopIntent())
            }
            is Action.Pause, Action.Stop -> {
                builder.setContentText("Time - paused")
                builder.addAction(R.drawable.ic_baseline_play_arrow_24, "PLAY", playIntent())
                builder.addAction(R.drawable.ic_baseline_stop_24, "STOP", stopIntent())
            }
        }
    }

    fun display(action: Action? = null) {
        action?.let {
            buildNotification(action)
        }

        NotificationManagerCompat.from(context).notify(
            NOTIFICATION_ID,
            builder.build()
        )
    }

    private fun playIntent(): PendingIntent {
        val intent = Intent(context, MyBroadcastReceiver::class.java)
            .setAction(INTENT_PLAY)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PLAY,
            intent,
            INTENT_FLAGS
        )
    }

    private fun pauseIntent(): PendingIntent {
        val intent = Intent(context, MyBroadcastReceiver::class.java)
            .setAction(INTENT_PAUSE)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PLAY,
            intent,
            INTENT_FLAGS
        )
    }

    private fun stopIntent(): PendingIntent {
        val intent = Intent(context, MyBroadcastReceiver::class.java)
            .setAction(INTENT_STOP)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_STOP,
            intent,
            INTENT_FLAGS
        )
    }

    companion object {
        private const val CHANNEL_ID = "com.emenjivar.pomodoro.counter"
        private const val CHANNEL_NAME = "pomodoro counter"
        private const val CHANNEL_DESCRIPTION = "Counter"

        private const val ICON = R.drawable.ic_baseline_play_arrow_24
        private const val INTENT_FLAGS =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        private const val NOTIFICATION_ID = 129909

        const val INTENT_PLAY = "playAction"
        const val INTENT_PAUSE = "pauseAction"
        const val INTENT_STOP = "stopAction"

        const val REQUEST_CODE_PLAY = 10
        const val REQUEST_CODE_STOP = 11

        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = CHANNEL_DESCRIPTION
                }

                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }
    }
}
