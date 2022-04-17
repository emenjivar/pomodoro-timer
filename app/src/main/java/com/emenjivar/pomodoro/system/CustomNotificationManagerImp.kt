package com.emenjivar.pomodoro.system

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.utils.Action
import com.emenjivar.pomodoro.utils.formatTime

class CustomNotificationManagerImp(private val context: Context) : CustomNotificationManager {

    lateinit var builder: NotificationCompat.Builder

    init {
        buildNotification()
    }

    override fun updateProgress(counter: Counter, action: Action?) {
        val phase = if (counter.phase == Phase.WORK) "Work time" else "Rest time"
        val totalTime = 100
        val progress = counter.getProgress().toInt()
        val formatTime = counter.countDown.formatTime()

        when (action) {
            Action.Play -> {
                builder
                    .setContentTitle(phase)
                    .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
                    .setContentText(formatTime)
                    .setProgress(totalTime, progress, false)
                    .clearActions()
                    .addAction(R.drawable.ic_baseline_pause_24, "PAUSE", pauseIntent())
                    .addAction(R.drawable.ic_baseline_stop_24, "STOP", stopIntent())
            }
            Action.Pause -> {
                builder
                    .setContentTitle("$phase - paused")
                    .setSmallIcon(R.drawable.ic_baseline_pause_24)
                    .setContentText(formatTime)
                    .setProgress(totalTime, progress, false)
                    .clearActions()
                    .addAction(R.drawable.ic_baseline_play_arrow_24, "PLAY", playIntent())
                    .addAction(R.drawable.ic_baseline_stop_24, "STOP", stopIntent())
            }
            else -> {
            }
        }

        NotificationManagerCompat.from(context).notify(
            NOTIFICATION_ID,
            builder.build()
        )
    }

    override fun close() {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }

    private fun buildNotification() {
        builder = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_baseline_play_arrow_24)
            .setContentTitle("Pomodoro")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
    }

    private fun playIntent(): PendingIntent {
        val intent = Intent(context, CustomBroadcastReceiver::class.java)
            .setAction(INTENT_PLAY)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PLAY,
            intent,
            INTENT_FLAGS
        )
    }

    private fun pauseIntent(): PendingIntent {
        val intent = Intent(context, CustomBroadcastReceiver::class.java)
            .setAction(INTENT_PAUSE)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_PAUSE,
            intent,
            INTENT_FLAGS
        )
    }

    private fun stopIntent(): PendingIntent {
        val intent = Intent(context, CustomBroadcastReceiver::class.java)
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

        private const val INTENT_FLAGS =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        private const val NOTIFICATION_ID = 129909

        const val INTENT_PLAY = "playAction"
        const val INTENT_PAUSE = "pauseAction"
        const val INTENT_STOP = "stopAction"

        const val REQUEST_CODE_PLAY = 10
        const val REQUEST_CODE_PAUSE = 11
        const val REQUEST_CODE_STOP = 12

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
