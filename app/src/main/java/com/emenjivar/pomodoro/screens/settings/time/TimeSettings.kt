package com.emenjivar.pomodoro.screens.settings.time

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.emenjivar.pomodoro.components.Item
import com.emenjivar.pomodoro.components.ItemGroup
import com.emenjivar.pomodoro.components.time.InputTime
import com.emenjivar.pomodoro.utils.millisecondsToMinutes
import org.koin.androidx.compose.getViewModel

@Composable
fun TimeSettings() {
    val viewModel = getViewModel<SettingsTimeViewModel>()
    LaunchedEffect(true) {
        viewModel.loadTimeValuesOnSettings()
    }

    TimeSettings(
        hours = viewModel.hours.value,
        minutes = viewModel.minutes.value,
        seconds = viewModel.seconds.value,
        workTime = viewModel.workTime.value,
        restTime = viewModel.restTime.value,
        openWorkTimeModal = { viewModel.loadTimeOnModal(isPomodoro = true) },
        openRestTimeModal = { viewModel.loadTimeOnModal(isPomodoro = false) },
        onInputChange = { viewModel.onInputChange(it) },
        onBackSpace = { viewModel.onBackSpace() },
        onSaveLoadedTime = { viewModel.saveTime(it) }
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

    ItemGroup(title = "Time settings") {
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
                text = "${workTime.millisecondsToMinutes()} min.",
                style = MaterialTheme.typography.subtitle1
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
                text = "${restTime.millisecondsToMinutes()} min.",
                style = MaterialTheme.typography.subtitle1
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