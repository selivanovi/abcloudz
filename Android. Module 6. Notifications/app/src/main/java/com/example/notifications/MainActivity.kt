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
            importance = NotificationCompat.PRIORITY_DEFAULT
        )

        Log.d("MainActivity", "Create channel")

        val itemId = 0

        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.forest, options)

        val notification = NotificationHelper.createNotificationFirstType(
            context = applicationContext,
            notificationId = itemId,
            title = getString(R.string.notification_first_title, itemId + 1),
            text = getString(R.string.notofication_first_text),
            smallIcon = R.drawable.message_icon,
            bitmap = bitmap,
            NotificationHelper.MAIN_CHANNEL_ID
        )

        Log.d("MainActivity", "Create notification: $notification")

        NotificationHelper.showNotification(applicationContext, itemId, notification)

    }
}