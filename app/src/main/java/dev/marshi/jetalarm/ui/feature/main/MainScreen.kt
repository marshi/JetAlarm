package dev.marshi.jetalarm.ui.feature.main

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.marshi.jetalarm.ui.JetAlarmBottomBar
import dev.marshi.jetalarm.ui.JetAlarmNavHost
import dev.marshi.jetalarm.ui.LocalNavigator
import dev.marshi.jetalarm.ui.Nav
import dev.marshi.jetalarm.ui.editalarm.showTimePicker

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = LocalNavigator.current
    val context = LocalContext.current
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
        JetAlarmNavHost(navController = navController)
    }
}

