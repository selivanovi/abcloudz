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
        Log.d("MyService", "OnCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyService", "OnStartCommand")

        if (NotificationHelper.ACTION_REPLY == intent?.action) {
            val results = RemoteInput.getResultsFromIntent(intent)

            results?.let {
                textReply = it.getCharSequence(NotificationHelper.KEY_TEXT_REPLY) as String
            }

            val itemId = intent.getIntExtra(NotificationHelper.KEY_ITEM_ID, 0)

            val notification = NotificationCompat.Builder(baseContext, NotificationHelper.MAIN_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Replied")
                .setContentText(textReply)
                .setGroup(NotificationHelper.REPLY_GROUP)
                .build()

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(itemId + 200, notification)
            notificationManager.cancel(itemId)
            // Stop service
            stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d("MyService", "onDestroy")
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? = null

    companion object {

        fun newIntent(context: Context) =
            Intent(context, ReplyService::class.java)
    }
}