package dev.marshi.jetalarm.ui.expandablecard

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExpandableCard(modifier: Modifier = Modifier) {
    var state by remember {
        mutableStateOf(
            ExpandableCardState("aiueo", expand = false)
        )
    }

    Card(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            state = state.copy(
                expand = !state.expand
            )
        }
    ) {
        Column {
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth()
                    .animateContentSize(),
                text = state.text,
            )
            if (state.expand) {
                Text(text = "expandddd")
            }
        }
    }
}

@Preview
@Composable
fun ExpandableCardPreview() {
    ExpandableCard()
}
