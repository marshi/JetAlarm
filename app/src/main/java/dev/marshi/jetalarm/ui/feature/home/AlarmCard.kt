package dev.marshi.jetalarm.ui.feature.home

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
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.marshi.jetalarm.domain.model.Alarm
import dev.marshi.jetalarm.ui.editalarm.showTimePicker
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

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
    onActivate: (id: Int, isActive: Boolean) -> Unit,
    onDelete: () -> Unit,
    onDayOfWeekButtonClick: (DayOfWeek, Boolean) -> Unit,
    onTimeSet: (Alarm) -> Unit,
) {
    val state by remember {
        mutableStateOf(
            AlarmCardState(initialExpanded = initialExpanded)
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
            AlarmTime(alarm = alarm, onTimeSet = onTimeSet)
            Row {
                Text(text = "月")
                Text(text = "水")
                Text(text = "木")
            }
            Switch(checked = alarm.isActive, onCheckedChange = { onActivate(alarm.id, it) })
            AnimatedVisibility(
                visible = state.isExpanded(),
                enter = fadeIn(animationSpec = tween(delayMillis = 300)) + expandVertically()
            ) {
                Column {
                    DayOfWeeks(
                        dayOfWeeks = alarm.dayOfWeeks,
                        onDayOfWeekClick = onDayOfWeekButtonClick,
                    )
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
    dayOfWeek: DayOfWeek,
    active: Boolean,
    onClick: (DayOfWeek) -> Unit,
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
        onClick = { onClick(dayOfWeek) }
    ) {
        Text(text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.JAPANESE), fontSize = 11.sp)
    }
}

@Composable
fun DayOfWeeks(
    dayOfWeeks: Set<DayOfWeek>,
    onDayOfWeekClick: (DayOfWeek, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        DayOfWeek.values().forEachIndexed { i, dayOfWeek ->
            DayOfWeekButton(
                dayOfWeek = dayOfWeek,
                active = dayOfWeeks.contains(dayOfWeek),
                onClick = { dayOfWeek ->
                    onDayOfWeekClick(
                        dayOfWeek,
                        !dayOfWeeks.contains(dayOfWeek)
                    )
                }
            )
        }
    }
}

@Composable
fun AlarmTime(
    alarm: Alarm,
    onTimeSet: (Alarm) -> Unit
) {
    val context = LocalContext.current
    TextButton(onClick = {
        showTimePicker(
            context = context,
            hour = alarm.hour,
            minute = alarm.minute,
            onTimeSet = { _, hour, minute ->
                onTimeSet(alarm.copy(hour = hour, minute = minute))
            })
    }) {
        Text(text = alarm.timeStr, fontSize = 64.sp)
    }
}

data class AlarmCardState(
    val initialExpanded: Boolean = false
) {

    private var expand by mutableStateOf(initialExpanded)

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
        alarm = Alarm(id = 0, hour = 9, minute = 0),
        initialExpanded = false,
        onDelete = {},
        onClick = {},
        onDayOfWeekButtonClick = { _, _ -> },
        onActivate = { _, _ -> },
        onTimeSet = {}
    )
}

@Preview(name = "expand")
@Composable
fun AlarmCardPreview2() {
    val alarm by remember {
        mutableStateOf(
            Alarm(
                id = 0,
                hour = 9,
                minute = 0,
                dayOfWeeks = setOf(DayOfWeek.MONDAY),
                isActive = true
            )
        )
    }

    AlarmCard(
        alarm = alarm,
        initialExpanded = true,
        onDelete = {},
        onClick = {},
        onDayOfWeekButtonClick = { _, _ -> },
        onActivate = { _, _ -> },
        onTimeSet = {}
    )
}

@Preview
@Composable
fun DayOfWeekButtonPreview() {
    var state by remember {
        mutableStateOf(false)
    }
    DayOfWeekButton(
        dayOfWeek = DayOfWeek.MONDAY,
        active = state,
        onClick = { state = !state },
    )
}