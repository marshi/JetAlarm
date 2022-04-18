package dev.marshi.jetalarm.ui.main

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import dev.marshi.jetalarm.ui.JetAlarmBottomBar
import dev.marshi.jetalarm.ui.JetAlarmNavHost
import dev.marshi.jetalarm.ui.Nav

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    Scaffold(
        bottomBar = { JetAlarmBottomBar(Nav.navs) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.addAlarm()
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        JetAlarmNavHost(navController = navController, mainViewModel = viewModel)
    }
}