package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CancelReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (NotificationHelper.ACTION_CANCEL == intent?.action){
            val itemId = intent.getIntExtra(NotificationHelper.KEY_ITEM_ID, 0)

            context?.let {
                NotificationHelper.cancelNotification(it, itemId)

            }
        }
    }

    companion object {
        fun newIntent(context: Context) =
            Intent(context, CancelReceiver::class.java)
    }
}
