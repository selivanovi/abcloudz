package com.example.notifications

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        NotificationHelper.createChannel(
            context = applicationContext,
            channelId = MAIN_CHANNEL_ID,
            name = getString(R.string.main_channel_name),
            description = getString(R.string.main_channel_description),
            importance = NotificationCompat.PRIORITY_MAX
        )

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainActivity", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log
            Log.d("MainActivity", "Token -> $token")
        })

    }

    override fun onStart() {
        Log.d("MainActivity", "onStart")
        startService(NotificationService.newIntent(applicationContext))
        super.onStart()
    }

    override fun onStop() {
        Log.d("MainActivity", "OnStop")
        stopService(NotificationService.newIntent(applicationContext))
        super.onStop()
    }
}