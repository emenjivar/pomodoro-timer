package com.emenjivar.pomodoro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.emenjivar.pomodoro.ui.screens.countdown.CountDownScreen
import com.emenjivar.pomodoro.ui.screens.settings.SettingsScreen

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "counter"
    ) {
        composable("counter") {
            CountDownScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }
    }
}
