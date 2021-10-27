package com.example.notifications

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.RemoteInput


class ReplyService : Service() {

    override fun onCreate() {
        Log.d("ReplyService", "OnCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("ReplyService", "OnStartCommand")
        if (ACTION_REPLY == intent?.action) {

            val results = RemoteInput.getResultsFromIntent(intent)

            val textReply = results.getString(KEY_TEXT_REPLY, "")
            val itemId = intent.getIntExtra(KEY_ITEM_ID, 0)
            val titleReply = intent.getStringExtra(KEY_TITLE) ?: getString(R.string.replied)

            notify(applicationContext, itemId, titleReply, textReply)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun notify(context: Context, itemId: Int, title: String, text: String) {

        val notification = NotificationHelper.createNotificationSecondType(
            context,
            title,
            text,
            R.drawable.message_icon,
            MAIN_CHANNEL_ID,
            REPLY_GROUP
        )

        NotificationHelper.cancelNotification(context, itemId)

        NotificationHelper.showNotification(
            context,
            REPLY_ID_NOTIFICATIONS,
            notification
        )

        REPLY_ID_NOTIFICATIONS++
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