package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput


object NotificationHelper {

    const val KEY_TEXT_REPLY = "key_text_reply"
    const val KEY_ITEM_ID = "item_id"

    const val MAIN_CHANNEL_ID = "main_channel_id"
    const val REPLY_GROUP = "reply_group"
    const val MAIN_GROUP = "main_group"
    const val ACTION_REPLY = "action_reply"
    const val ACTION_CANCEL = "action_reply"


    fun createChannel(
        context: Context,
        channelId: String,
        name: String,
        description: String,
        importance: Int,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(channelId, name, importance).apply {
                this.description = description
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotificationFirstType(
        context: Context,
        notificationId: Int,
        title: String,
        text: String,
        smallIcon: Int,
        bitmap: Bitmap,
        channelId: String
    ): Notification {

        val replyIntent =
            configureIntent(notificationId, ACTION_REPLY, ReplyService.newIntent(context))
        val replyPendingIntent = createPendingIntent(context, notificationId, replyIntent)

        val cancelIntent =
            configureIntent(notificationId, ACTION_CANCEL, CancelReceiver.newIntent(context))
        val cancelPendingIntent = createPendingIntent(context, notificationId, cancelIntent)

        val cancelAction =
            NotificationCompat.Action.Builder(
                R.drawable.close_icon,
                context.getString(R.string.cancel),
                cancelPendingIntent
            ).build()

        // Remote Input
        val remoteInput: RemoteInput =
            RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(context.getString(R.string.remote_input_text))
                .build()

        // Action
        val replyAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            context.getString(R.string.reply),
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()

        // Create notification
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(text)
            .setLargeIcon(bitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null)
            )
            .setGroup(MAIN_GROUP)
            .addAction(replyAction)
            .addAction(cancelAction)
            .setOngoing(true)
            .build()
    }

    fun createNotificationSecondType() {

    }

    fun showNotification(context: Context, notificationId: Int, notification: Notification) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }

    private fun createPendingIntent(
        context: Context,
        requestCode: Int,
        intent: Intent
    ): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun configureIntent(id: Int, action: String, intent: Intent) =
        intent.apply {
            this.action = action
            putExtra(KEY_ITEM_ID, id)
        }


}