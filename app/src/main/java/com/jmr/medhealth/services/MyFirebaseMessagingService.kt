package com.jmr.medhealth.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM_SERVICE"

    /**
     * Called when a new token for the device is generated.
     * This is the unique ID you would send to your server to target this device for push notifications.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        // TODO: Send this token to your application server (e.g., save it in the user's profile in Firestore).
    }

    /**
     * Called when a message is received while the app is in the foreground.
     * Here, you would build and display a custom notification.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // Here you would typically use NotificationManagerCompat to build and show a notification.
        }
    }
}