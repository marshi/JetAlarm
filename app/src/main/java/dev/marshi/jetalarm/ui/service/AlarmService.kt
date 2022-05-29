package dev.marshi.jetalarm.ui.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dev.marshi.jetalarm.R
import dev.marshi.jetalarm.ui.notification.AlarmReceiver
import dev.marshi.jetalarm.ui.notification.AlarmReceiverBundle

class AlarmService : Service() {

    private val player = MediaPlayer()

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        player.stop()
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle =
            intent?.getParcelableExtra<AlarmServiceBundle>("bundle") ?: return START_NOT_STICKY

        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        if (bundle.operation == AlarmOperation.Stop) {
            stopSelf(startId)
        }
        player.apply {
            setDataSource(this@AlarmService, uri)
            setAudioAttributes(AudioAttributes.Builder().build())
            isLooping = true
            prepare()
        }
        player.start()

        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.KEY_BUNDLE, AlarmReceiverBundle.StopAlarm)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            AlarmReceiver.REQUEST_STOP,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, AlarmReceiver.PRIMARY_CHANNEL_ID)
            .setContentTitle("title")
            .setContentText("contenttext")
            .setSmallIcon(R.mipmap.ic_launcher)
            .addAction(R.mipmap.ic_launcher, "action", pendingIntent)
            .build()
        startForeground(1, notification)
        return START_STICKY
    }
}