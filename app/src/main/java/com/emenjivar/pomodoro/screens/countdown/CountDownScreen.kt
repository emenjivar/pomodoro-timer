package com.emenjivar.pomodoro.screens.countdown

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.utils.TRANSITION_DURATION

@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    viewModel: CountDownViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    playAction: () -> Unit,
    pauseAction: () -> Unit,
    stopAction: () -> Unit,
    fullScreenAction: () -> Unit
) {
    val counter by viewModel.counter.observeAsState(Counter())
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val isFullScreen by viewModel.isFullScreen.observeAsState(false)

    CountDownScreen(
        modifier = modifier,
        isPlaying = isPlaying,
        counter = counter,
        playAction = playAction,
        pauseAction = pauseAction,
        stopAction = stopAction,
        fullScreenAction = fullScreenAction,
        isFullScreen = isFullScreen
    )
}

@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    counter: Counter,
    playAction: () -> Unit,
    pauseAction: () -> Unit,
    stopAction: () -> Unit,
    fullScreenAction: () -> Unit,
    isFullScreen: Boolean = false
) {
    val horizontalSpace = 30.dp

    val playPauseIcon = if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
    val fullScreenIcon = if (isFullScreen) R.drawable.ic_baseline_wb_sunny_24 else R.drawable.ic_baseline_mode_night_24
    val backgroundColor = animateColorAsState(
        targetValue = colorResource(if (isFullScreen) R.color.primary else R.color.white),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )
    val playPauseAction = if (isPlaying) playAction else pauseAction

    Column(
        modifier = modifier
            .background(backgroundColor.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CountDown(
            modifier = Modifier
                .padding(top = 50.dp),
            time = counter.time,
            progress = counter.progress,
            size = 230,
            stroke = 7,
            isFullScreen = isFullScreen
        )

        Row(
            modifier = Modifier
                .padding(vertical = 25.dp)
        ) {
            ActionButton(icon = playPauseIcon, isFullScreen = isFullScreen, onClick = playPauseAction)
            Spacer(modifier = Modifier.width(horizontalSpace))
            ActionButton(icon = R.drawable.ic_baseline_stop_24, isFullScreen = isFullScreen, onClick = stopAction)
            Spacer(modifier = Modifier.width(horizontalSpace))
            ActionButton(icon = fullScreenIcon, isFullScreen = isFullScreen, onClick = fullScreenAction)
        }
    }
}

@Preview(name = "Normal counter")
@Composable
fun PreviewCountDownScreen() {
    CountDownScreen(
        modifier = Modifier.fillMaxSize(),
        isPlaying = false,
        counter = Counter(time = "24:59", progress =  0.99f),
        playAction = {},
        pauseAction = {},
        stopAction = {},
        fullScreenAction = {},
        isFullScreen = false
    )
}

@Preview(name = "Full screen counter")
@Composable
fun PreviewCountDownFullScreen() {
    CountDownScreen(
        modifier = Modifier.fillMaxSize(),
        isPlaying = false,
        counter = Counter(time = "24:59", progress =  0.99f),
        playAction = {},
        pauseAction = {},
        stopAction = {},
        fullScreenAction = {},
        isFullScreen = true
    )
}