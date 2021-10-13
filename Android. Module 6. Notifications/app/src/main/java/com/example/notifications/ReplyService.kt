package com.example.notifications

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.RemoteInput


class ReplyService : Service() {

    private lateinit var textReply: String

    override fun onCreate() {
        Log.d("ReplyService", "OnCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("ReplyService", "OnStartCommand")

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
                textReply = it.getString(NotificationHelper.KEY_TEXT_REPLY, "")
            }

            val itemId = intent.getIntExtra(NotificationHelper.KEY_ITEM_ID, 0)
            val titleReply = intent.getStringExtra(NotificationHelper.KEY_TITLE) ?: getString(R.string.replied)

            val notification = NotificationHelper.createNotificationSecondType(
                applicationContext,
                NotificationHelper.REPLY_ID_NOTIFICATIONS,
                titleReply,
                textReply,
                R.drawable.message_icon,
                NotificationHelper.MAIN_CHANNEL_ID,
                NotificationHelper.REPLY_GROUP
            )

            NotificationHelper.cancelNotification(applicationContext, itemId)
            NotificationHelper.showNotification(
                applicationContext,
                NotificationHelper.REPLY_ID_NOTIFICATIONS,
                notification
            )
            NotificationHelper.REPLY_ID_NOTIFICATIONS++
        }
    }

    override fun onDestroy() {
        Log.d("ReplyService", "onDestroy")
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {

        fun newIntent(context: Context) =
            Intent(context, ReplyService::class.java)
    }
}