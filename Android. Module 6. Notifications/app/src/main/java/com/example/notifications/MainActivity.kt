package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        NotificationHelper.createChannel(
            context = applicationContext,
            channelId = NotificationHelper.MAIN_CHANNEL_ID,
            name = getString(R.string.main_channel_name),
            description = getString(R.string.main_channel_description),
            importance = NotificationCompat.PRIORITY_MAX
        )

    }

    override fun onStart() {
        startService(NotificationService.newIntent(applicationContext))

        super.onStart()
    }

    override fun onStop() {
        stopService(NotificationService.newIntent(applicationContext))
        super.onStop()
    }
}