package dev.marshi.jetalarm.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import dev.marshi.jetalarm.ui.feature.alarmresult.AlarmResultScreen
import dev.marshi.jetalarm.ui.feature.main.MainScreen
import dev.marshi.jetalarm.ui.theme.JetAlarmTheme

@Composable
fun JetAlarmApp(viewModel: JetAlarmAppViewModel = hiltViewModel()) {

    LaunchedEffect(true) {
        viewModel.observeAndUpdateLatestAlarm()
    }

    JetAlarmTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "main") {
            composable("main") {
                MainScreen()
            }
            composable(
                "alarm_result",
                deepLinks = listOf(navDeepLink {
                    uriPattern = "https://dev.marshi.jetalarm/alarmresult"
                })
            ) {
                AlarmResultScreen()
            }
        }

    }
}

val LocalNavigator = compositionLocalOf<NavHostController> { error("navigation not found") }

