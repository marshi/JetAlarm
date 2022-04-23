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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.marshi.jetalarm.ui.model.Alarm

@Composable
fun AlarmListScreen(
    viewModel: AlarmViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    var prevSize = remember { state.alarms.size }
    val isAdded by derivedStateOf {
        val isAdded = prevSize < state.alarms.size
        prevSize = state.alarms.size
        return@derivedStateOf isAdded
    }

    Surface {
        AlarmList(
            alarms = state.alarms,
            scrollToTop = isAdded,
            onDelete = { alarm ->
                viewModel.remove(alarm)
            },
            onTimeSet = { alarm ->
                viewModel.update(alarm)
            }
        )
    }
}

@Composable
fun AlarmList(
    alarms: List<Alarm>,
    scrollToTop: Boolean,
    onDelete: (alarm: Alarm) -> Unit,
    onTimeSet: (Alarm) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val isExpandedSet = remember { mutableSetOf<Long>() }

    LaunchedEffect(alarms) {
        if (scrollToTop) {
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
                    onTimeSet = onTimeSet
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
            scrollToTop = false,
            onDelete = {
                alarms.removeAt(alarms.indexOf(it))
            },
            onTimeSet = {}
        )
    }
}
