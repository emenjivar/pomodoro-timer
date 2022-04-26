package com.emenjivar.pomodoro.system

import android.content.Context
import android.media.MediaPlayer
import com.emenjivar.pomodoro.R

class SoundManagerImp(private val context: Context) : SoundsManager {

    private var mediaPlayer = MediaPlayer.create(context, R.raw.discrete_bell)

    override fun play() {
        mediaPlayer.isLooping = false
        mediaPlayer.start()
    }
}
