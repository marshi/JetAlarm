package dev.marshi.jetalarm.ui.feature.main

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.marshi.jetalarm.ui.JetAlarmBottomBar
import dev.marshi.jetalarm.ui.LocalNavigator
import dev.marshi.jetalarm.ui.Nav
import dev.marshi.jetalarm.ui.editalarm.showTimePicker
import dev.marshi.jetalarm.ui.feature.home.AlarmListScreen
import dev.marshi.jetalarm.ui.feature.log.LogScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavigator provides navController) {
        Scaffold(
            bottomBar = { JetAlarmBottomBar(Nav.navs) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showTimePicker(context, onTimeSet = { _, hour, minute ->
                            viewModel.addAlarm(hour = hour, minute = minute)
                        })
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        ) {
            JetAlarmMainNavHost(navController = navController)
        }
    }
}

@Composable
fun JetAlarmMainNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            AlarmListScreen()
        }
        composable("log") { LogScreen() }
    }
}
