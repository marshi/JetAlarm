package dev.marshi.jetalarm.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.marshi.jetalarm.ui.home.AlarmListScreen
import dev.marshi.jetalarm.ui.log.LogScreen
import dev.marshi.jetalarm.ui.main.MainScreen
import dev.marshi.jetalarm.ui.theme.JetAlarmTheme

@Composable
fun JetAlarmApp() {
    JetAlarmTheme {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavigator provides navController) {
            MainScreen()
        }
    }
}

val LocalNavigator = compositionLocalOf<NavHostController> { error("navigation not found") }

@Composable
fun JetAlarmNavHost(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            AlarmListScreen()
        }
        composable("log") { LogScreen() }
    }
}
