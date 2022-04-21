package com.emenjivar.pomodoro.system

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class CustomVibrationImp(private val context: Context) : CustomVibrator {

    @SuppressLint("WrongConstant")
    override fun vibrate() {
        val vibrator = getVibrator()

        // Api 26 code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    VIBRATION_TIME,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else { // Api 25 or less
            @Suppress("DEPRECATION")
            vibrator.vibrate(VIBRATION_TIME)
        }
    }

    @SuppressLint("WrongConstant")
    private fun getVibrator() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Api 31
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else { // Api 30 or less
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator
        }

    companion object {
        private const val VIBRATION_TIME: Long = 500
    }
}
