package com.emenjivar.pomodoro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.emenjivar.pomodoro.ui.screens.countdown.CountDownScreen
import com.emenjivar.pomodoro.ui.screens.settings.SettingsScreen
import com.google.accompanist.systemuicontroller.SystemUiController

@Composable
fun MainNavHost(
    navController: NavHostController,
    systemUiController: SystemUiController
) {
    NavHost(
        navController = navController,
        startDestination = "counter"
    ) {
        composable("counter") {
            CountDownScreen(
                navController = navController,
                systemUiController = systemUiController
            )
        }

        composable("settings") {
            SettingsScreen(
                navController = navController,
                systemUiController = systemUiController
            )
        }
    }
}
