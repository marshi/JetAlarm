package dev.marshi.jetalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.marshi.jetalarm.ui.AlarmListScreen
import dev.marshi.jetalarm.ui.JetAlarmApp
import dev.marshi.jetalarm.ui.theme.JetAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetAlarmApp()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetAlarmTheme {
    }
}