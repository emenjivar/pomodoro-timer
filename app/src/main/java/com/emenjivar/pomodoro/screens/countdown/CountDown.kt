package com.emenjivar.pomodoro.screens.countdown

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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

@Composable
fun CountDown(
    modifier: Modifier = Modifier,
    time: String,
    progress: Float,
    size: Int,
    stroke: Int,
    isFullScreen: Boolean = false
) {
    val itemColor = colorResource(if(isFullScreen) R.color.white else R.color.primary)

    Column(modifier = modifier) {
        Box {
            CircularProgressIndicatorBackground(
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = colorResource(R.color.light),
                stroke = stroke
            )

            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = itemColor,
                strokeWidth = stroke.dp
            )

            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = time,
                    color = itemColor,
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
    val itemColor = colorResource(if(isFullScreen) R.color.white else R.color.primary)
    val iconColor = colorResource(if(isFullScreen) R.color.primary else R.color.white)

    IconButton(
        onClick = onClick,
        modifier = Modifier
            .clip(CircleShape)
            .background(itemColor)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = iconColor
        )
    }
}