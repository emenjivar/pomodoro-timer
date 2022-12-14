package com.emenjivar.pomodoro.data.model

import com.emenjivar.pomodoro.utils.toSafeInt

data class StructTime(
    val hours: String,
    val minutes: String,
    val seconds: String,
    val timeString: String
) {

    fun getMilliseconds(): Long =
        1000 * (3600 * hours.toSafeInt() + 60 * minutes.toSafeInt() + seconds.toSafeInt()).toLong()

    companion object {
        private const val EMPTY_TIME = "00"
        fun empty() = StructTime(
            hours = EMPTY_TIME,
            minutes = EMPTY_TIME,
            seconds = EMPTY_TIME,
            timeString = "000000"
        )
    }
}