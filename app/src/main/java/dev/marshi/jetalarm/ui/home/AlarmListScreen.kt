package dev.marshi.jetalarm.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.internal.aggregatedroot.codegen._dev_marshi_jetalarm_App
import dev.marshi.jetalarm.ui.model.Alarm
import java.time.DayOfWeek

@Composable
fun AlarmListScreen(
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    AlarmListScreen(
        state = state,
        onDelete = { viewModel.remove(it) },
        onDayOfWeekClick = { id, dayOfWeek, active ->
            viewModel.updateDayOfWeek(id, dayOfWeek, active)
        },
        onTimeSet = { viewModel.update(it) }
    )
}


@Composable
fun AlarmListScreen(
    state: AlarmListState,
    onDelete: (Alarm) -> Unit,
    onDayOfWeekClick: (id: Long, dayOfWeek: DayOfWeek, active: Boolean) -> Unit,
    onTimeSet: (Alarm) -> Unit
) {
    Surface {
        Column {
            AlarmList(
                alarms = state.alarms,
                onDelete = onDelete,
                onDayOfWeekClicked = onDayOfWeekClick,
                onTimeSet = onTimeSet,
            )
        }
    }
}

@Composable
fun AlarmList(
    alarms: List<Alarm>,
    onDelete: (alarm: Alarm) -> Unit,
    onDayOfWeekClicked: (id: Long, DayOfWeek, Boolean) -> Unit,
    onTimeSet: (Alarm) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val isExpandedSet = remember { mutableSetOf<Long>() }
    var prevSize by remember { mutableStateOf(alarms.size) }
    val isAdded by derivedStateOf {
        val isAdded = prevSize < alarms.size
        prevSize = alarms.size
        return@derivedStateOf isAdded
    }

    LaunchedEffect(alarms) {
        if (isAdded) {
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
                    initialExpanded = isExpandedSet.contains(alarm.id),
                    onClick = { isExpanded ->
                        if (isExpanded) {
                            isExpandedSet.add(alarm.id)
                        } else {
                            isExpandedSet.remove(alarm.id)
                        }
                    },
                    onDelete = {
                        onDelete(alarm)
                    },
                    onTimeSet = onTimeSet,
                    onDayOfWeekButtonClick = { dayOfWeek, active ->
                        onDayOfWeekClicked(alarm.id, dayOfWeek, active)
                    },
                )
            }
        }
    }
}

@Preview
@Composable
fun AlarmListScreenPreview() {
    val state by remember {
        mutableStateOf(
            AlarmListState(
                alarms = listOf(
                    Alarm(id = 1, hour = 9, minute = 0),
                    Alarm(id = 2, hour = 10, minute = 0),
                    Alarm(id = 3, hour = 11, minute = 0),
                )
            )
        )
    }
    AlarmListScreen(
        state = state,
        onDayOfWeekClick = { _, _, _ -> },
        onDelete = {},
        onTimeSet = {}
    )
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
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    alarms.add(Alarm.now())
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        AlarmList(
            alarms = alarms,
            onDelete = {
                alarms.removeAt(alarms.indexOf(it))
            },
            onDayOfWeekClicked = { _, _, _ -> },
            onTimeSet = {},
        )
    }
}
