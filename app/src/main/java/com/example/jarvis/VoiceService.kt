package com.example.jarvis

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder

class VoiceService : Service() {
    override fun onCreate() {
        super.onCreate()
        val channelId = "jarvis_voice"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(channelId, "Jarvis voice control", NotificationManager.IMPORTANCE_LOW)
            )
        }
        startForeground(42, Notification.Builder(this, channelId)
            .setContentTitle("Jarvis is ready")
            .setContentText("Voice control is active")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
}
