package com.emenjivar.pomodoro.countdown

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.model.Counter

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

    CountDownScreen(
        modifier = modifier,
        isPlaying = isPlaying,
        counter = counter,
        playAction = playAction,
        pauseAction = pauseAction,
        stopAction = stopAction,
        fullScreenAction = fullScreenAction
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
    fullScreenAction: () -> Unit
) {
    val horizontalSpace = 30.dp

    val playPauseIcon = if (isPlaying) {
        R.drawable.ic_baseline_pause_24
    } else {
        R.drawable.ic_baseline_play_arrow_24
    }

    val playPauseAction = if (isPlaying) playAction else pauseAction

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountDown(
            modifier = Modifier
                .padding(top = 50.dp),
            time = counter.time,
            progress = counter.progress,
            size = 230,
            stroke = 7
        )

        Row(
            modifier = Modifier
                .padding(vertical = 25.dp)
        ) {
            ActionButton(icon = playPauseIcon, onClick = playPauseAction)
            Spacer(modifier = Modifier.width(horizontalSpace))
            ActionButton(icon = R.drawable.ic_baseline_stop_24, onClick = stopAction)
            Spacer(modifier = Modifier.width(horizontalSpace))
            ActionButton(icon = R.drawable.ic_baseline_fullscreen_24, onClick = fullScreenAction)
        }
    }
}

@Preview
@Composable
fun PreviewCountDownScreen() {
    CountDownScreen(
        modifier = Modifier.fillMaxSize(),
        isPlaying = false,
        counter = Counter(time = "24:59", progress =  0.99f),
        playAction = {},
        pauseAction = {},
        stopAction = {},
        fullScreenAction = {}
    )
}