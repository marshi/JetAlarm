package dev.marshi.jetalarm.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.marshi.jetalarm.ui.model.Alarm

@Composable
fun AlarmListScreen(vm: AlarmViewModel = hiltViewModel()) {
    val state = remember {
        mutableStateListOf(
            Alarm(time = "9:00"),
            Alarm(time = "10:00"),
            Alarm(time = "11:00")
        )
    }

    Surface {
        AlarmList(
            alarms = state,
            onDelete = { index ->
                state.removeAt(index)
            }
        )
    }
}

@Composable
fun AlarmList(
    alarms: List<Alarm>,
    onDelete: (index: Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        alarms.forEachIndexed { index, alarm ->
            item(key = alarm.id) {
                AlarmCard(
                    alarm = alarm,
                    backgroundColor = Color.Gray,
                    onDelete = {
                        onDelete(index)
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
            Alarm(time = "9:00"),
            Alarm(time = "10:00"),
            Alarm(time = "11:00")
        )
    }
    Surface {
        AlarmList(
            alarms = alarms,
            onDelete = {
                alarms.removeAt(it)
            }
        )
    }
}
