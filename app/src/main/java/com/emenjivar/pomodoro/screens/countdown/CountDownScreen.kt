package com.emenjivar.pomodoro.screens.countdown

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.model.NormalPomodoro
import com.emenjivar.pomodoro.model.Pomodoro
import com.emenjivar.pomodoro.utils.TRANSITION_DURATION

@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    viewModel: CountDownViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    playAction: () -> Unit,
    pauseAction: () -> Unit,
    stopAction: () -> Unit,
    fullScreenAction: () -> Unit,
    openSettings: () -> Unit
) {
    val pomodoro by viewModel.pomodoro.observeAsState(NormalPomodoro() as Pomodoro)
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val isFullScreen by viewModel.isFullScreen.observeAsState(false)

    CountDownScreen(
        modifier = modifier,
        isPlaying = isPlaying,
        pomodoro = pomodoro,
        playAction = playAction,
        pauseAction = pauseAction,
        stopAction = stopAction,
        fullScreenAction = fullScreenAction,
        isFullScreen = isFullScreen,
        openSettings = openSettings
    )
}

@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    pomodoro: Pomodoro,
    playAction: () -> Unit,
    pauseAction: () -> Unit,
    stopAction: () -> Unit,
    fullScreenAction: () -> Unit,
    isFullScreen: Boolean = false,
    openSettings: () -> Unit,
) {
    val horizontalSpace = 30.dp

    val playPauseIcon =
        if (isPlaying) R.drawable.ic_baseline_pause_24
        else R.drawable.ic_baseline_play_arrow_24

    val fullScreenIcon =
        if (isFullScreen) R.drawable.ic_baseline_wb_sunny_24
        else R.drawable.ic_baseline_mode_night_24

    val backgroundColor = animateColorAsState(
        targetValue = colorResource(if (isFullScreen) R.color.primary else R.color.white),
        animationSpec = tween(TRANSITION_DURATION)
    )
    val iconColor = animateColorAsState(
        targetValue = colorResource(if (isFullScreen) R.color.white else R.color.primary),
        animationSpec = tween(TRANSITION_DURATION)
    )
    val playPauseAction = if (isPlaying) playAction else pauseAction

    ConstraintLayout(
        modifier = modifier
            .background(backgroundColor.value)
    ) {
        val (_settingsButton, _container) = createRefs()

        IconButton(
            modifier = Modifier
                .constrainAs(_settingsButton) {
                    top.linkTo(anchor = parent.top, margin = 16.dp)
                    end.linkTo(anchor = parent.end, margin = 16.dp)
                },
            onClick = { openSettings() }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_settings_24),
                tint = iconColor.value,
                contentDescription = null
            )
        }

        Column(
            modifier = Modifier
                .constrainAs(_container) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CountDown(
                modifier = Modifier
                    .padding(top = 50.dp),
                time = pomodoro.time,
                progress = pomodoro.progress,
                size = 230,
                stroke = 7,
                isFullScreen = isFullScreen
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 25.dp)
            ) {
                ActionButton(
                    icon = playPauseIcon,
                    isFullScreen = isFullScreen,
                    onClick = playPauseAction
                )
                Spacer(modifier = Modifier.width(horizontalSpace))
                ActionButton(
                    icon = R.drawable.ic_baseline_stop_24,
                    isFullScreen = isFullScreen,
                    onClick = stopAction
                )
                Spacer(modifier = Modifier.width(horizontalSpace))
                ActionButton(
                    icon = fullScreenIcon,
                    isFullScreen = isFullScreen,
                    onClick = fullScreenAction
                )
            }
        }
    }
}

@Preview(name = "Normal counter")
@Composable
fun PreviewCountDownScreen() {
    CountDownScreen(
        modifier = Modifier.fillMaxSize(),
        isPlaying = false,
        pomodoro = NormalPomodoro(time = "24:59", progress = 0.99f),
        playAction = {},
        pauseAction = {},
        stopAction = {},
        fullScreenAction = {},
        isFullScreen = false,
        openSettings = {}
    )
}

@Preview(name = "Full screen counter")
@Composable
fun PreviewCountDownFullScreen() {
    CountDownScreen(
        modifier = Modifier.fillMaxSize(),
        isPlaying = false,
        pomodoro = NormalPomodoro(time = "24:59", progress = 0.99f),
        playAction = {},
        pauseAction = {},
        stopAction = {},
        fullScreenAction = {},
        isFullScreen = true,
        openSettings = {}
    )
}
