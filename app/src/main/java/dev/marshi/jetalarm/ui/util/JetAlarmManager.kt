package dev.marshi.jetalarm.ui.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dev.marshi.jetalarm.domain.model.Alarm
import dev.marshi.jetalarm.ui.notification.AlarmReceiver
import dev.marshi.jetalarm.ui.notification.AlarmReceiverBundle

object JetAlarmManager {


    fun setAlarm(context: Context, alarm: Alarm) {
        setAlarm(context, alarm.id, alarm.timeInMillis)
    }

    fun setNextAlarm(context: Context, alarm: Alarm) {
        setAlarm(context, alarm.id, alarm.nextDayTimeInMillis)
    }

    private fun setAlarm(context: Context, id: String, timeInMillis: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.KEY_BUNDLE, AlarmReceiverBundle(id = id))
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent,
        )
//        AlarmManagerCompat.setAlarmClock(
//            alarmManager,
//            cal.timeInMillis,
//            pendingIntent,
//            pendingIntent
//        )
    }
}
