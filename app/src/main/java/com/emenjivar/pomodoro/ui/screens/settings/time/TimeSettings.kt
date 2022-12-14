package com.emenjivar.pomodoro.ui.screens.settings.time

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.emenjivar.pomodoro.data.model.StructTime
import com.emenjivar.pomodoro.ui.components.Item
import com.emenjivar.pomodoro.ui.components.ItemTitle
import com.emenjivar.pomodoro.ui.components.time.InputTime
import com.emenjivar.pomodoro.utils.toSplitTime
import org.koin.androidx.compose.getViewModel

@Composable
fun TimeSettings(
    workTime: Long,
    restTime: Long,
    structTime: StructTime,
    loadModalTime: (isPomodoro: Boolean) -> Unit,
    onInputChange: (Int) -> Unit,
    onBackSpace: () -> Unit,
    onSaveTime: (isPomodoro: Boolean) -> Unit
) {
    TimeSettings(
        hours = structTime.hours,
        minutes = structTime.minutes,
        seconds = structTime.seconds,
        workTime = workTime,
        restTime = restTime,
        openWorkTimeModal = {
            loadModalTime(true)
        },
        openRestTimeModal = {
            loadModalTime(false)
        },
        onInputChange = { onInputChange(it) },
        onBackSpace = { onBackSpace() },
        onSaveLoadedTime = { onSaveTime(it) }
    )
}

@Composable
fun TimeSettings(
    hours: String,
    minutes: String,
    seconds: String,
    workTime: Long,
    restTime: Long,
    openWorkTimeModal: () -> Unit,
    openRestTimeModal: () -> Unit,
    onInputChange: (Int) -> Unit,
    onBackSpace: () -> Unit,
    onSaveLoadedTime: (isPomodoro: Boolean) -> Unit
) {
    val showCustomDialog = remember { mutableStateOf(false) }
    val isPomodoro = remember { mutableStateOf(true) }
    val modalTitle = remember { mutableStateOf("Pomodoro time") }
    val workTimeSplit = workTime.toSplitTime()
    val restTimeSplit = restTime.toSplitTime()

    Column {
        ItemTitle(title = "Time settings")
        Item(
            title = "Work",
            subtitle = "Defines the duration of the interval",
            onClick = {
                showCustomDialog.value = true
                isPomodoro.value = true
                modalTitle.value = "Pomodoro time"
                openWorkTimeModal()
            }
        ) {
            Text(
                text = "${workTimeSplit.hours}:${workTimeSplit.minutes}:${workTimeSplit.seconds}",
                style = MaterialTheme.typography.caption
            )
        }

        Item(
            title = "Rest",
            subtitle = "Defines the duration of the rest interval",
            onClick = {
                showCustomDialog.value = true
                isPomodoro.value = false
                modalTitle.value = "Rest time"
                openRestTimeModal()
            }
        ) {
            Text(
                text = "${restTimeSplit.hours}:${restTimeSplit.minutes}:${restTimeSplit.seconds}",
                style = MaterialTheme.typography.caption
            )
        }
    }

    if (showCustomDialog.value) {
        InputTime(
            title = modalTitle.value,
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            isPomodoro = isPomodoro.value,
            onInputChange = onInputChange,
            onBackSpace = onBackSpace,
            onDismiss = { showCustomDialog.value = false },
            onSaveLoadedTime = {
                onSaveLoadedTime(it)
                showCustomDialog.value = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeSettings() {
    MaterialTheme {
        TimeSettings(
            hours = "25",
            minutes = "00",
            seconds = "00",
            workTime = 1500000,
            restTime = 300000, // 5 min
            openWorkTimeModal = { },
            openRestTimeModal = { },
            onInputChange = {},
            onBackSpace = { },
            onSaveLoadedTime = {}
        )
    }
}
