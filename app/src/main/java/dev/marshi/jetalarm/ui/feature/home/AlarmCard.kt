package dev.marshi.jetalarm.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.marshi.jetalarm.domain.model.Alarm
import dev.marshi.jetalarm.ui.editalarm.showTimePicker
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@OptIn(
    ExperimentalMaterialApi::class,
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                DayOfWeekAnnotation(alarm = alarm)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = alarm.isActive,
                    onCheckedChange = { onActivate(alarm.id, it) }
                )
            }
            AnimatedVisibility(
                visible = state.isExpanded(),
                enter = fadeIn(animationSpec = tween(delayMillis = 300)) + expandVertically()
            ) {
                Column {
                    DayOfWeekButtons(
                        dayOfWeeks = alarm.dayOfWeeks,
                        enabled = alarm.isActive,
                        onDayOfWeekClick = onDayOfWeekButtonClick,
                    )
                    DeleteButton(onDelete = onDelete)
                }
            }
        }
    }
}

@Composable
fun DayOfWeekButton(
    dayOfWeek: DayOfWeek,
    enabled: Boolean,
    active: Boolean,
    onClick: (DayOfWeek) -> Unit,
) {
    Button(
        modifier = Modifier.size(24.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        enabled = enabled,
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
fun DayOfWeekButtons(
    enabled: Boolean = true,
    dayOfWeeks: Set<DayOfWeek>,
    onDayOfWeekClick: (DayOfWeek, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DayOfWeek.values().forEachIndexed { i, dayOfWeek ->
            DayOfWeekButton(
                dayOfWeek = dayOfWeek,
                active = dayOfWeeks.contains(dayOfWeek),
                enabled = enabled,
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
    TextButton(
        contentPadding = PaddingValues(0.dp),
        enabled = alarm.isActive,
        onClick = {
            showTimePicker(
                context = context,
                hour = alarm.hour,
                minute = alarm.minute,
                onTimeSet = { _, hour, minute ->
                    onTimeSet(alarm.copy(hour = hour, minute = minute))
                }
            )
        },
    ) {
        Text(text = alarm.timeStr, fontSize = 64.sp)
    }
}

@Composable
fun DayOfWeekAnnotation(alarm: Alarm) {
    val dayOfWeeks = alarm.dayOfWeeks
    val alpha = if (alarm.isActive) {
        LocalContentAlpha.current
    } else {
        ContentAlpha.disabled
    }
    CompositionLocalProvider(LocalContentAlpha provides alpha) {
        if (dayOfWeeks.isEmpty()) {
            Text("未設定")
        } else {
            dayOfWeeks.mapIndexed { index, dayOfWeek ->
                Text(text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.JAPANESE))
                if (index != dayOfWeeks.size - 1) {
                    Text("、")
                }
            }
        }
    }
}

@Composable
fun DeleteButton(onDelete: () -> Unit) {
    IconButton(onClick = onDelete) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "delete this alarm"
            )
            Text("削除")
        }
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

class AlarmCardParameter(
    val alarm: Alarm,
    val isExpanded: Boolean,
)

class PreviewAlarmCardParameterProvider : PreviewParameterProvider<AlarmCardParameter> {
    override val values: Sequence<AlarmCardParameter>
        get() = sequenceOf(
            AlarmCardParameter(
                alarm = Alarm(
                    id = 0,
                    hour = 9,
                    dayOfWeeks = setOf(DayOfWeek.MONDAY),
                    isActive = true
                ),
                isExpanded = false
            ),
            AlarmCardParameter(
                alarm = Alarm(
                    id = 0,
                    hour = 9,
                    minute = 0,
                    dayOfWeeks = setOf(DayOfWeek.MONDAY),
                    isActive = true
                ),
                isExpanded = true
            ),
            AlarmCardParameter(
                alarm = Alarm(
                    id = 1,
                    hour = 19,
                    minute = 0,
                    dayOfWeeks = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                    isActive = false
                ),
                isExpanded = true
            ),
            AlarmCardParameter(
                alarm = Alarm(
                    id = 1,
                    hour = 19,
                    minute = 0,
                    dayOfWeeks = setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY),
                    isActive = false
                ),
                isExpanded = false
            ),
        )
}


@Preview
@Composable
fun AlarmCardPreview2(
    @PreviewParameter(PreviewAlarmCardParameterProvider::class) params: AlarmCardParameter,
) {
    AlarmCard(
        alarm = params.alarm,
        initialExpanded = params.isExpanded,
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
        enabled = true,
        active = state,
        onClick = { state = !state },
    )
}