package com.emenjivar.pomodoro

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CountDown(
    modifier: Modifier = Modifier,
    time: String,
    progress: Float,
    size: Int,
    stroke: Int
) {
    Column(modifier = modifier) {
        Box {
            CircularProgressIndicatorBackground(
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = colorResource(id = R.color.light),
                stroke = stroke
            )

            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = colorResource(id = R.color.primary),
                strokeWidth = stroke.dp
            )

            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = time,
                    color = Color(0xFFFF5C58),
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
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .clip(CircleShape)
            .background(colorResource(id = R.color.primary))
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null
        )
    }
}

@Composable
fun CountDownLayout(
    modifier: Modifier = Modifier,
    time: String,
    progress: Float,
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
            time = time,
            progress = progress,
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

@Preview(showBackground = true)
@Composable
fun PreviewCountDownLayout() {
    CountDownLayout(
        modifier = Modifier.fillMaxSize(),
        time = "25:00",
        progress = 0.98f,
        pauseAction = {},
        stopAction = {},
        fullScreenAction = {}
    )
}