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
import com.emenjivar.pomodoro.model.Counter
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.utils.Action
import com.emenjivar.pomodoro.utils.TRANSITION_DURATION
import com.emenjivar.pomodoro.utils.formatTime

/**
 * @param selectedColor value from mainActivity, it would be probably
 *  this value reduce fetch delay when color is selected different to primary
 */
@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    countDownViewModel: CountDownViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    selectedColor: Int? = null
) {
    val viewModelSelectedColor by countDownViewModel.selectedColor.observeAsState()
    val themeColor = viewModelSelectedColor ?: selectedColor

    val counter by countDownViewModel.counter
    val action by countDownViewModel.action.observeAsState()

    CountDownScreen(
        modifier = modifier,
        action = action,
        counter = counter,
        selectedColor = themeColor,
        playAction = { countDownViewModel.startCounter() },
        pauseAction = { countDownViewModel.pauseCounter() },
        resumeAction = { countDownViewModel.resumeCounter() },
        stopAction = { countDownViewModel.stopCounter() },
        openSettings = { countDownViewModel.openSettings() }
    )
}

@Composable
fun CountDownScreen(
    modifier: Modifier = Modifier,
    action: Action?,
    counter: Counter?,
    selectedColor: Int? = null,
    playAction: () -> Unit,
    pauseAction: () -> Unit,
    resumeAction: () -> Unit,
    stopAction: () -> Unit,
    isNightMode: Boolean = true,
    openSettings: () -> Unit,
) {
    val nextAction = when (action) {
        Action.Play -> pauseAction
        Action.Pause -> resumeAction
        Action.Resume -> pauseAction
        Action.Stop -> playAction
        else -> playAction
    }

    ConstraintLayout(
        modifier = modifier
            .background(getBackgroundColor(selectedColor).value)
    ) {
        val (_settingsButton, _container) = createRefs()

        IconButton(
            modifier = Modifier
                .constrainAs(_settingsButton) {
                    top.linkTo(anchor = parent.top, margin = 16.dp)
                    end.linkTo(anchor = parent.end, margin = 16.dp)
                },
            onClick = openSettings
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_settings_24),
                tint = getIconColor(true).value,
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
                progressColor = selectedColor,
                time = counter?.countDown.formatTime(),
                progress = counter?.getScaleProgress() ?: 1f,
                phase = counter?.phase,
                action = action,
                isFullScreen = isNightMode
            )

            Row(
                modifier = Modifier
                    .padding(vertical = 25.dp)
            ) {
                ActionButton(
                    icon = getPlayPauseIcon(action),
                    isFullScreen = isNightMode,
                    selectedColor = selectedColor,
                    onClick = nextAction
                )
                Spacer(modifier = Modifier.width(50.dp))
                ActionButton(
                    icon = R.drawable.ic_baseline_stop_24,
                    isFullScreen = isNightMode,
                    selectedColor = selectedColor,
                    onClick = stopAction
                )
            }
        }
    }
}

private fun getPlayPauseIcon(action: Action?) = when (action) {
    Action.Play, Action.Resume -> R.drawable.ic_baseline_pause_24
    else -> R.drawable.ic_baseline_play_arrow_24
}

@Composable
private fun getBackgroundColor(selectedColor: Int?) = animateColorAsState(
    targetValue = colorResource(selectedColor ?: R.color.primary),
    animationSpec = tween(TRANSITION_DURATION)
)

@Composable
private fun getIconColor(isNightMode: Boolean) = animateColorAsState(
    targetValue = colorResource(if (isNightMode) R.color.white else R.color.primary),
    animationSpec = tween(TRANSITION_DURATION)
)

@Preview(name = "Normal counter")
@Composable
fun PreviewCountDownScreen() {
    CountDownScreen(
        modifier = Modifier.fillMaxSize(),
        action = Action.Play,
        counter = Counter(WORK_TIME, REST_TIME, Phase.WORK, WORK_TIME),
        playAction = {},
        pauseAction = {},
        resumeAction = {},
        stopAction = {},
        openSettings = {}
    )
}

@Preview(name = "Paused counter")
@Composable
fun PreviewPausedCountDown() {
    CountDownScreen(
        modifier = Modifier.fillMaxSize(),
        action = Action.Pause,
        counter = Counter(WORK_TIME, REST_TIME, Phase.WORK, WORK_TIME),
        playAction = {},
        pauseAction = {},
        resumeAction = {},
        stopAction = {},
        openSettings = {}
    )
}

private const val WORK_TIME: Long = 1000 * 60 * 25
private const val REST_TIME: Long = 1000 * 60 * 5
