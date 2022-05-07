package dev.marshi.jetalarm.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import dev.marshi.jetalarm.MainActivity
import dev.marshi.jetalarm.R
import dev.marshi.jetalarm.data.AlarmRepository
import dev.marshi.jetalarm.di.SingletonScope
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
        scope.launch {
            val alarm = alarmRepository.find(alarmBundle.id) ?: return@launch
            JetAlarmManager.setNextAlarm(context, alarm)
            val nowDayOfWeek = LocalDateTime.now().dayOfWeek
            if (!alarm.dayOfWeek.contains(nowDayOfWeek)) {
                return@launch
            }
            createNotificationChannel()
            deliverNotification(context)
        }
    }

    private fun deliverNotification(context: Context) {
        val contentIntent = Intent(
            Intent.ACTION_VIEW,
            "https://dev.marshi.jetalarm/alarmresult".toUri(),
            context,
            MainActivity::class.java,
        )
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Alert")
            .setContentText("This is repeating alarm")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
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