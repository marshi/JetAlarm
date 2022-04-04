package dev.marshi.jetalarm.ui

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
import dev.marshi.jetalarm.ui.model.Alarm

@Composable
fun AlarmListScreen() {

    val state = remember {
        mutableStateListOf(
            Alarm(time = "9:00"),
            Alarm(time = "10:00"),
            Alarm(time = "11:00")
        )
    }

    Surface {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.forEachIndexed { index, alarm ->
                item(key = alarm.id) {
                    AlarmCard(
                        alarm = alarm,
                        backgroundColor = Color.Gray,
                        onDelete = {
                            state.removeAt(index)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AlarmListScreenPreview() {
    AlarmListScreen()
}
