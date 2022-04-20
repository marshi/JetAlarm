package dev.marshi.jetalarm.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.marshi.jetalarm.ui.main.AddAlarm
import dev.marshi.jetalarm.ui.main.MainViewModel
import dev.marshi.jetalarm.ui.model.Alarm

@Composable
fun AlarmListScreen(
    viewModel: AlarmViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val mainState by mainViewModel.state.collectAsState()

    Surface {
        AlarmList(
            alarms = state.alarms,
            addAlarms = mainState.addAlarms,
            onDelete = { alarm ->
                viewModel.remove(alarm)
            },
            onAdded = { addAlarm ->
                viewModel.add(addAlarm.alarm)
                mainViewModel.added(addAlarm.id)
            }
        )
    }
}

@Composable
fun AlarmList(
    alarms: List<Alarm>,
    addAlarms: List<AddAlarm>,
    onDelete: (alarm: Alarm) -> Unit,
    onAdded: (AddAlarm) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    addAlarms.forEach { addAlarm ->
        onAdded(addAlarm)
    }
    LaunchedEffect(addAlarms) {
        addAlarms.firstOrNull().let { addAlarm ->
            lazyListState.animateScrollToItem(0)
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        alarms.forEachIndexed { index, alarm ->
            item(key = alarm.id) {
                AlarmCard(
                    alarm = alarm,
                    backgroundColor = Color.Gray,
                    onDelete = {
                        onDelete(alarm)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AlarmListPreview() {
    var alarms = remember {
        mutableStateListOf(
            Alarm(id = 1, hour = 9, minute = 0),
            Alarm(id = 2, hour = 10, minute = 0),
            Alarm(id = 3, hour = 11, minute = 0),
        )
    }
    var addAlarms by remember { mutableStateOf(listOf<AddAlarm>()) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                addAlarms = addAlarms + AddAlarm(alarm = Alarm(hour = 10, minute = 20))
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        println(alarms)
        AlarmList(
            alarms = alarms,
            addAlarms = addAlarms,
            onDelete = {
                alarms.removeAt(alarms.indexOf(it))
            },
            onAdded = { addAlarm ->
                alarms.add(addAlarm.alarm)
                addAlarms = addAlarms.filterNot { it.id == addAlarm.id }
            }
        )
    }
}
