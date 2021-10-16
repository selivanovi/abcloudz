package com.example.notifications

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log

class NotificationService : Service() {

    override fun onCreate() {
        Log.d("NotificationService", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("NotificationService", "onStartCommand")
        var quantity = 0
        Thread {
            while (quantity < QUANTITY_OF_NOTIFICATIONS) {
                sendMessage(applicationContext, quantity)
                quantity++
                Thread.sleep(15000)
            }
        }.start()
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun sendMessage(context: Context, n: Int) {
        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.forest, options)

        val notification = NotificationHelper.createNotificationFirstType(
            context = applicationContext,
            notificationId = NOTIFICATION_ID,
            title = getString(R.string.notification_first_title, n + 1),
            text = getString(R.string.notofication_first_text),
            smallIcon = R.drawable.message_icon,
            bitmap = bitmap,
            channelId = MAIN_CHANNEL_ID,
            groupId = MAIN_GROUP
        )

        NotificationHelper.showNotification(context, NOTIFICATION_ID, notification)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {
        var QUANTITY_OF_NOTIFICATIONS = 5
        private const val NOTIFICATION_ID = 0

        fun newIntent(context: Context) =
            Intent(context, NotificationService::class.java)
    }
}