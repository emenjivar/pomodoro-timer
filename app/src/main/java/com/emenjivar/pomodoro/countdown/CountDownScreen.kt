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
    pauseAction: () -> Unit,
    stopAction: () -> Unit,
    fullScreenAction: () -> Unit
) {
    val counter by viewModel.counter.observeAsState(Counter.default())
    CountDownScreen(
        modifier = modifier,
        counter = counter,
        pauseAction = pauseAction,
        stopAction = stopAction,
        fullScreenAction = fullScreenAction
    )
}

@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    counter: Counter,
    pauseAction: () -> Unit,
    stopAction: () -> Unit,
    fullScreenAction: () -> Unit
) {
    val horizontalSpace = 30.dp
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
            ActionButton(icon = R.drawable.ic_baseline_pause_24, onClick = pauseAction)
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
        counter = Counter("24:59", 0.99f),
        pauseAction = {},
        stopAction = {},
        fullScreenAction = {}
    )
}