package com.example.notifications

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput


class ReplyService : Service() {

    private lateinit var textReply: String

    override fun onCreate() {
        Log.d("MyApp", "OnCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("MyApp", "OnStartCommand")

        intent?.let {
            Thread {
                notify(it)
            }.start()
        }
        stopSelf()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun notify(intent: Intent) {
        if (NotificationHelper.ACTION_REPLY == intent?.action) {
            val results = RemoteInput.getResultsFromIntent(intent)

            results?.let {
                textReply = it.getCharSequence(NotificationHelper.KEY_TEXT_REPLY) as String
            }

            val itemId = intent.getIntExtra(NotificationHelper.KEY_ITEM_ID, 0)

            val newNotificationId = itemId + 100

            val notification = NotificationHelper.createNotificationSecondType(
                applicationContext,
                newNotificationId,
                getString(R.string.replied),
                textReply,
                R.drawable.message_icon,
                NotificationHelper.MAIN_CHANNEL_ID,
                NotificationHelper.REPLY_GROUP
            )

            NotificationHelper.cancelNotification(applicationContext, itemId)
            NotificationHelper.showNotification(applicationContext, newNotificationId, notification)
        }
    }

    override fun onDestroy() {
        Log.d("MyApp", "onDestroy")
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {

        fun newIntent(context: Context) =
            Intent(context, ReplyService::class.java)
    }
}