package dev.marshi.jetalarm.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.marshi.jetalarm.ui.model.Alarm

@OptIn(
    ExperimentalUnitApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun AlarmCard(
    modifier: Modifier = Modifier,
    alarm: Alarm,
    initialExpanded: Boolean = false,
    backgroundColor: Color = MaterialTheme.colors.surface,
    onClick: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    val state by remember {
        mutableStateOf(
            AlarmCardState(alarm = alarm, initialExpanded = initialExpanded)
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        indication = rememberRipple(),
        backgroundColor = backgroundColor,
        onClick = {
            state.toggleExpand()
            onClick(state.isExpanded())
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = alarm.timeStr, fontSize = 64.sp)
            Row {
                Text(text = "月")
                Text(text = "水")
                Text(text = "木")
            }
            AnimatedVisibility(
                visible = state.isExpanded(),
                enter = fadeIn(animationSpec = tween(delayMillis = 300)) + expandVertically()
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("月", "火", "水", "木", "金", "土", "日").forEachIndexed { i, dayOfWeek ->
                            DayOfWeekButton(
                                text = dayOfWeek,
                                active = state.dayOfWeekActive(i),
                                onClick = {
                                    state.toggleEnable(i)
                                }
                            )
                        }
                    }
                    Text("aiueo")
                    Text("aiueo")
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete this alarm"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DayOfWeekButton(
    text: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.size(24.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (active) {
                Color.Green
            } else {
                Color.Gray
            }
        ),
        onClick = onClick
    ) {
        Text(text = text, fontSize = 11.sp)
    }
}

data class AlarmCardState(
    val alarm: Alarm,
    val initialExpanded: Boolean = false
) {

    private val dayOfWeekEnable = mutableStateListOf(*List(7) { false }.toTypedArray())

    private var expand by mutableStateOf(initialExpanded)

    fun dayOfWeekActive(index: Int) = dayOfWeekEnable[index]

    fun toggleEnable(index: Int) {
        require(index in 0..6)
        dayOfWeekEnable[index] = !dayOfWeekEnable[index]
    }

    fun toggleExpand() {
        expand = !expand
    }

    fun isExpanded(): Boolean {
        return expand
    }
}

@Preview(name = "shrink")
@Composable
fun AlarmCardPreview1() {
    AlarmCard(
        alarm = Alarm(id = 0L, hour = 9, minute = 0),
        initialExpanded = false,
        onDelete = {},
        onClick = {})
}

@Preview(name = "expand")
@Composable
fun AlarmCardPreview2() {
    AlarmCard(
        alarm = Alarm(id = 0, hour = 9, minute = 0),
        initialExpanded = true,
        onDelete = {},
        onClick = {})
}

@Preview
@Composable
fun DayOfWeekButtonPreview() {
    var state by remember {
        mutableStateOf(false)
    }
    DayOfWeekButton(
        text = "月",
        active = state,
        onClick = {
            state = !state
        }
    )
}