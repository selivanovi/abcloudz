package com.example.notifications


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val remote = remoteMessage.notification ?: return

        val id = FIREBASE_NOTIFICATION_ID
        val title = remote.title ?: getString(R.string.title)
        val text = remote.title ?: getString(R.string.text)

        val notification =
            NotificationHelper.createNotificationSecondType(
                applicationContext,
                title,
                text,
                R.drawable.ic_launcher_foreground,
                MAIN_CHANNEL_ID,
                FIREBASE_GROUP
            )

        NotificationHelper.showNotification(applicationContext, id, notification)

        super.onMessageReceived(remoteMessage)
    }
}