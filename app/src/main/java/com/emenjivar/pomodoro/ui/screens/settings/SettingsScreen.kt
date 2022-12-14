package com.emenjivar.pomodoro.ui.screens.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.Text
import androidx.compose.material.IconButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.emenjivar.pomodoro.R
import com.emenjivar.pomodoro.data.model.StructTime
import com.emenjivar.pomodoro.ui.screens.common.ColorMenu
import com.emenjivar.pomodoro.ui.screens.settings.time.TimeSettings
import com.emenjivar.pomodoro.ui.theme.lightGray
import com.emenjivar.pomodoro.utils.TRANSITION_DURATION
import com.emenjivar.pomodoro.utils.ThemeColor
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.getViewModel

/**
 * @param selectedColor reduce ui color delay, loading this value
 *  while viewModel finish to fetch the color
 */
@Composable
fun SettingsScreen(
    navController: NavController,
    systemUiController: SystemUiController
) {
    val viewModel = getViewModel<SettingsViewModel>()
    val colorTheme by viewModel.uiState.colorTheme.collectAsState()
    val workTime by viewModel.uiState.workTime.collectAsState()
    val restTime by viewModel.uiState.restTime.collectAsState()
    val autoPlay by viewModel.autoPlay
    val keepScreenOn by viewModel.keepScreenOn

    DisposableEffect(systemUiController, colorTheme) {
        systemUiController.setStatusBarColor(color = colorTheme)
        onDispose { }
    }

    SettingsScreen(
        uiState = viewModel.uiState,
        selectedColor = colorTheme,
        workTime = workTime,
        restTime = restTime,
        autoPlay = autoPlay,
        keepScreenOn = keepScreenOn,
        backAction = { navController.navigate("counter") },
        onSelectTheme = { viewModel.setColor(it) },
        onAutoPlayChange = {
            viewModel.setAutoPlay(it)
        },
        onKeepScreenChange = {
            viewModel.setKeepScreenOn(it)
        }
    )
}

@Composable
fun SettingsScreen(
    uiState: SettingsUIState,
    selectedColor: Color,
    workTime: Long,
    restTime: Long,
    autoPlay: Boolean,
    keepScreenOn: Boolean,
    backAction: () -> Unit,
    onSelectTheme: (Color) -> Unit,
    onAutoPlayChange: (Boolean) -> Unit,
    onKeepScreenChange: (Boolean) -> Unit
) {
    val structTime by uiState.structTime.collectAsState()
    val enableSound by uiState.enableSound.collectAsState()
    val enableVibration by uiState.enableVibration.collectAsState()

    val topAppBarColor = animateColorAsState(
        targetValue = selectedColor,
        animationSpec = tween(durationMillis = TRANSITION_DURATION)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(onClick = backAction) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_24),
                            contentDescription = null
                        )
                    }
                },
                backgroundColor = topAppBarColor.value,
                contentColor = colorResource(R.color.white)
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .background(MaterialTheme.colors.background)
        ) {
            AppearanceSettings(
                selectedColor = selectedColor,
                onSelectTheme = onSelectTheme
            )

            TimeSettings(
                workTime = workTime,
                restTime = restTime,
                structTime = structTime,
                loadModalTime = uiState.loadModalTime,
                onInputChange = uiState.onInputChange,
                onBackSpace = uiState.onBackSpace,
                onSaveTime = uiState.onSaveTime
            )

            SoundSettings(
                selectedColor = selectedColor,
                enableVibration = enableVibration,
                enableSound = enableSound,
                onEnableVibration = uiState.onEnableVibration,
                onEnableSound = uiState.onEnableSound
            )

            OthersSettings(
                selectedColor = selectedColor,
                autoPlay = autoPlay,
                keepScreenOn = keepScreenOn,
                onAutoPlayChange = onAutoPlayChange,
                onKeepScreenChange = onKeepScreenChange
            )
        }
    }
}

@Composable
fun SettingsGroup(
    title: String,
    action: @Composable () -> Unit
) {
    Text(
        text = title,
        color = colorResource(id = R.color.primary),
        fontSize = 13.sp,
        modifier = Modifier.padding(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )
    )
    action()
}

@Composable
fun SwitchItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    value: Boolean,
    selectedColor: Color,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!value) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(2f)
        ) {
            Text(text = title)

            subtitle?.let { safeSubtitle ->
                Text(
                    text = safeSubtitle,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(
                        top = 8.dp
                    )
                )
            }
        }

        Column(
            modifier = Modifier.weight(0.8f),
            horizontalAlignment = Alignment.End
        ) {
            Switch(
                checked = value,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = selectedColor,
                    uncheckedThumbColor = lightGray
                )
            )
        }
    }
}

@Composable
private fun AppearanceSettings(
    selectedColor: Color?,
    onSelectTheme: (Color) -> Unit
) = SettingsGroup(title = "Appearance settings") {
    Row(modifier = Modifier.padding(16.dp)) {
        ColorMenu(
            selectedColor = selectedColor,
            onSelectTheme = onSelectTheme
        )
    }
}

@Composable
private fun SoundSettings(
    selectedColor: Color,
    enableVibration: Boolean,
    enableSound: Boolean,
    onEnableVibration: (Boolean) -> Unit,
    onEnableSound: (Boolean) -> Unit
) = SettingsGroup(title = "Sounds") {
    SwitchItem(
        title = "Tune",
        subtitle = "Play a small sound when after every interval on time",
        value = enableSound,
        selectedColor = selectedColor,
        onCheckedChange = onEnableSound
    )
    SwitchItem(
        title = "Vibration",
        value = enableVibration,
        selectedColor = selectedColor,
        onCheckedChange = onEnableVibration
    )
}

@Composable
private fun OthersSettings(
    selectedColor: Color,
    autoPlay: Boolean,
    keepScreenOn: Boolean,
    onAutoPlayChange: (Boolean) -> Unit,
    onKeepScreenChange: (Boolean) -> Unit,
) = SettingsGroup(title = "Others") {
    SwitchItem(
        title = "Autoplay",
        subtitle = "At the end of a pomodoro, the next one start automatically",
        value = autoPlay,
        selectedColor = selectedColor,
        onCheckedChange = onAutoPlayChange
    )
    SwitchItem(
        title = "Keep screen on",
        value = keepScreenOn,
        selectedColor = selectedColor,
        onCheckedChange = onKeepScreenChange
    )
}

val uiState = SettingsUIState(
    colorTheme = MutableStateFlow(ThemeColor.Tomato.color),
    workTime = MutableStateFlow(0L),
    restTime = MutableStateFlow(0L),
    structTime = MutableStateFlow(StructTime.empty()),
    enableSound = MutableStateFlow(true),
    enableVibration = MutableStateFlow(true),
    loadModalTime = {},
    onInputChange = {},
    onBackSpace = {},
    onSaveTime = {},
    onEnableSound = {},
    onEnableVibration = {}
)

@Preview(
    name = "Normal settings",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewSettingsItem() {
    MaterialTheme {
        SettingsScreen(
            uiState = uiState,
            selectedColor = ThemeColor.Tomato.color,
            workTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            backAction = {},
            onSelectTheme = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {}
        )
    }
}

@Preview(name = "Settings screen orange theme", showBackground = true)
@Composable
fun PreviewSettingsItemOrange() {
    MaterialTheme {
        SettingsScreen(
            uiState = uiState,
            selectedColor = ThemeColor.Orange.color,
            workTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            backAction = {},
            onSelectTheme = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {}
        )
    }
}

@Preview(name = "Settings screen green theme", showBackground = true)
@Composable
fun PreviewSettingsItemWine() {
    MaterialTheme {
        SettingsScreen(
            uiState = uiState,
            selectedColor = ThemeColor.Green.color,
            workTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            backAction = {},
            onSelectTheme = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {}
        )
    }
}

@Preview(name = "Settings screen leaf green theme", showBackground = true)
@Composable
fun PreviewSettingsItemBasil() {
    MaterialTheme {
        SettingsScreen(
            uiState = uiState,
            selectedColor = ThemeColor.LeafGreen.color,
            workTime = 0L,
            restTime = 0L,
            autoPlay = true,
            keepScreenOn = true,
            backAction = {},
            onSelectTheme = {},
            onAutoPlayChange = {},
            onKeepScreenChange = {}
        )
    }
}
