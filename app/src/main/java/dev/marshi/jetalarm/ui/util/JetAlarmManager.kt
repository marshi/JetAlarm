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

    private fun setAlarm(context: Context, id: Int, timeInMillis: Long) {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = createIntent(context = context, id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            pendingIntent,
        )
    }


    fun deleteAlarm(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = createIntent(context, id)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun createIntent(context: Context, id: Int): Intent {
        return Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.KEY_BUNDLE, AlarmReceiverBundle(id = id))
        }
    }
}
