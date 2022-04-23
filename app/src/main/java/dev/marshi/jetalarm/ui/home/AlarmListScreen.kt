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

    Surface {
        AlarmList(
            alarms = state.alarms,
            onDelete = { alarm ->
                viewModel.remove(alarm)
            },
        )
    }
}

@Composable
fun AlarmList(
    alarms: List<Alarm>,
    onDelete: (alarm: Alarm) -> Unit,
) {
    val lazyListState = rememberLazyListState()
    var prevSize = remember { alarms.size }
    val isExpandedSet = remember { mutableSetOf<Int>() }

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
                    initialExpanded = isExpandedSet.contains(index),
                    onClick = { isExpanded ->
                        if (isExpanded) {
                            isExpandedSet.add(index)
                        } else {
                            isExpandedSet.remove(index)
                        }
                    },
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
        )
    }
}
