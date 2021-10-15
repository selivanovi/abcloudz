package com.example.notifications

import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        remoteMessage.notification?.let {
            val id = NotificationHelper.FIREBASE_NOTIFICATION_ID
            val title = it.title ?: getString(R.string.title)
            val text = it.title ?: getString(R.string.text)

            val notification =
                NotificationHelper.createNotificationSecondType(
                    applicationContext,
                    title,
                    text,
                    R.drawable.ic_launcher_foreground,
                    NotificationHelper.MAIN_CHANNEL_ID,
                    NotificationHelper.FIREBASE_GROUP
                )

            NotificationHelper.showNotification(applicationContext, id, notification)
        }


        super.onMessageReceived(remoteMessage)
    }

}