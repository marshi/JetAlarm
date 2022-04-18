package dev.marshi.jetalarm.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.marshi.jetalarm.ui.main.AddAlarm
import dev.marshi.jetalarm.ui.main.MainViewModel
import dev.marshi.jetalarm.ui.model.Alarm
import kotlinx.coroutines.launch

@Composable
fun AlarmListScreen(
    viewModel: AlarmViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val mainState by mainViewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()

    mainState.addAlarms.firstOrNull()?.let { addAlarm ->
        viewModel.add(addAlarm.alarm)
        mainViewModel.added(addAlarm.id)
        viewModel.scrollToTop()
    }
    state.scroll.firstOrNull()?.let {
        scope.launch {
            lazyListState.animateScrollToItem(0)
        }
    }

    Surface {
        AlarmList(
            alarms = state.alarms,
            lazyListListState = lazyListState,
            onDelete = { alarm ->
                viewModel.remove(alarm)
            },
        )
    }
}

@Composable
fun AlarmList(
    alarms: List<Alarm>,
    lazyListListState: LazyListState,
    onDelete: (alarm: Alarm) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        state = lazyListListState,
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
    val alarms = remember {
        mutableStateListOf(
            Alarm(id = 1, hour = 9, minute = 0),
            Alarm(id = 2, hour = 10, minute = 0),
            Alarm(id = 3, hour = 11, minute = 0),
        )
    }
    val addAlarms = listOf(
        AddAlarm(
            alarm = Alarm(id = 4, hour = 11, minute = 0)
        )
    )
    Surface {
        AlarmList(
            alarms = alarms,
            lazyListListState = rememberLazyListState(),
            onDelete = {
                alarms.removeAt(alarms.indexOf(it))
            },
        )
    }
}
