package dev.marshi.jetalarm.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
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

@OptIn(
    ExperimentalUnitApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun AlarmCard(modifier: Modifier = Modifier) {

    val state by remember { mutableStateOf(AlarmCardState()) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        indication = rememberRipple(),
        onClick = {
            state.toggleExpand()
        }
    ) {
        Column() {
            Text("9:00", fontSize = 64.sp)
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
    val text: String = "",
) {

    private val dayOfWeekEnable = mutableStateListOf(*List(7) { false }.toTypedArray())

    private var expand by mutableStateOf(false)

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

@Preview
@Composable
fun AlarmCardPreview() {
    AlarmCard()
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