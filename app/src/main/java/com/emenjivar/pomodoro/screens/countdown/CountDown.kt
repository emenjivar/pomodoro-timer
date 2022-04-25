package com.emenjivar.pomodoro.screens.countdown

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.model.Phase
import com.emenjivar.pomodoro.utils.Action
import com.emenjivar.pomodoro.utils.TRANSITION_DURATION

/**
 * @param time expected a formatted milliseconds string, 60000 -> "1:00"
 * @param progress from 100.0 to 0.0
 * @param isFullScreen define the background and button colors
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountDown(
    modifier: Modifier = Modifier,
    time: String,
    progress: Float,
    phase: Phase? = Phase.WORK,
    action: Action? = Action.Play,
    size: Int = 230,
    stroke: Int = 7,
    isFullScreen: Boolean = false
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )
    val itemColor = animateColorAsState(
        targetValue = colorResource(if (isFullScreen) R.color.white else R.color.primary),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )
    val progressBackgroundColor = animateColorAsState(
        targetValue = colorResource(if (isFullScreen) R.color.primary else R.color.light),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )

    val phaseText = if (phase == Phase.WORK) "work time" else "rest time"

    val showPhaseText = when (action) {
        Action.Play -> true
        Action.Pause -> true
        Action.Resume -> true
        Action.Stop -> false
        else -> false
    }

    Column(modifier = modifier) {
        Box {
            CircularProgressIndicatorBackground(
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = progressBackgroundColor.value,
                stroke = stroke
            )

            CircularProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = itemColor.value,
                strokeWidth = stroke.dp
            )

            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = time,
                    color = itemColor.value,
                    fontFamily = FontFamily(Font(R.font.ubuntu_regular)),
                    fontSize = 70.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                AnimatedVisibility(
                    visible = showPhaseText,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = phaseText,
                        color = itemColor.value,
                        fontFamily = FontFamily(Font(R.font.ubuntu_regular)),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorBackground(
    modifier: Modifier = Modifier,
    color: Color,
    stroke: Int
) {
    val style = with(LocalDensity.current) {
        Stroke(stroke.dp.toPx())
    }

    Canvas(
        modifier = modifier
    ) {
        val innerRadius = (size.minDimension - style.width) / 2

        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            size = Size(innerRadius * 2, innerRadius * 2),
            topLeft = Offset(
                (size / 2.0f).width - innerRadius,
                (size / 2.0f).height - innerRadius
            ),
            style = style
        )
    }
}

@Composable
fun ActionButton(
    icon: Int,
    isFullScreen: Boolean = false,
    onClick: () -> Unit
) {
    val itemColor = animateColorAsState(
        targetValue = colorResource(if (isFullScreen) R.color.white else R.color.primary),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )
    val iconColor = animateColorAsState(
        targetValue = colorResource(if (isFullScreen) R.color.primary else R.color.white),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .clip(CircleShape)
            .background(itemColor.value)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconColor.value
        )
    }
}
