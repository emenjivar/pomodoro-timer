package com.emenjivar.pomodoro.screens.countdown

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
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
import com.emenjivar.pomodoro.utils.TRANSITION_DURATION

@Composable
fun CountDown(
    modifier: Modifier = Modifier,
    time: String,
    progress: Float,
    size: Int,
    stroke: Int,
    isFullScreen: Boolean = false
) {
    val itemColor = animateColorAsState(
        targetValue = colorResource(if(isFullScreen) R.color.white else R.color.primary),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )
    val progressBackgroundColor = animateColorAsState(
        targetValue = colorResource(if(isFullScreen) R.color.primary else R.color.light),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )

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
                progress = progress,
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
        targetValue = colorResource(if(isFullScreen) R.color.white else R.color.primary),
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )
    val iconColor = animateColorAsState(
        targetValue = colorResource(if(isFullScreen) R.color.primary else R.color.white),
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