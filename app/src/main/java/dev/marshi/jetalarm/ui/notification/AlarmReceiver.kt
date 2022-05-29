package dev.marshi.jetalarm.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import dagger.hilt.android.AndroidEntryPoint
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.di.SingletonScope
import dev.marshi.jetalarm.ui.service.AlarmOperation
import dev.marshi.jetalarm.ui.service.AlarmService
import dev.marshi.jetalarm.ui.service.AlarmServiceBundle
import dev.marshi.jetalarm.ui.util.JetAlarmManager
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver() : BroadcastReceiver() {
    companion object {
        const val TAG = "AlarmReceiver"
        const val NOTIFICATION_ID = 0
        const val PRIMARY_CHANNEL_ID = "alarm_notification_channel"
        const val KEY_BUNDLE = "alarm_receiver_bundle"

        const val REQUEST_START = 1
        const val REQUEST_STOP = 2
    }

    @Inject
    lateinit var scope: SingletonScope

    @Inject
    lateinit var alarmRepository: AlarmRepository

    lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        println("receive")
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmBundle = intent.extras?.getParcelable<AlarmReceiverBundle>(KEY_BUNDLE) ?: return
        when (alarmBundle) {
            is AlarmReceiverBundle.StartAlarm -> {
                onReceiveLaunchAlarm(context, alarmBundle)
            }
            is AlarmReceiverBundle.StopAlarm -> {
                val intent = Intent(context, AlarmService::class.java)
                context.stopService(intent)
            }
        }
    }

    private fun onReceiveLaunchAlarm(context: Context, startAlarm: AlarmReceiverBundle.StartAlarm) {
        scope.launch {
            val alarm = alarmRepository.find(startAlarm.id) ?: return@launch
            JetAlarmManager.setNextAlarm(context, alarm)
            val nowDayOfWeek = LocalDateTime.now().dayOfWeek
            if (!alarm.enabledOn(nowDayOfWeek)) {
                return@launch
            }
            createNotificationChannel()
            val intent = Intent(context, AlarmService::class.java).apply {
                putExtra("bundle", AlarmServiceBundle(AlarmOperation.Start))
            }
            context.startForegroundService(intent)
        }
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            "Stand up notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "AlarmManager Tests"
        notificationManager.createNotificationChannel(
            notificationChannel
        )
    }
}