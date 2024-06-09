package com.wesal.askhail.core.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.wesal.askhail.R
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.domain.useCases.UseCaseImpl
import java.net.HttpURLConnection
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @SuppressLint("LogNotTimber")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("FIREN","--> ${Gson().toJson(remoteMessage.notification)}")
        Log.e("FIRE D","--> ${remoteMessage.data.toString()}")
        var message:String? = ""
        var title:String? = ""
        if (remoteMessage.data.isNotEmpty()) {
            val type = remoteMessage.data["type"]
            title = remoteMessage.data["title"]
            val action_id = remoteMessage.data["action_id"]
            message = remoteMessage.data["message"]

        }
        //        Log.e("NOTIFICATIONB",new Gson().toJson(notification));
        var imageUrl: Uri? = null
        if (remoteMessage.notification != null) {
            imageUrl = remoteMessage.notification?.imageUrl
        }
        var bitmap: Bitmap? = null
        if (imageUrl != null) {
            bitmap = getBitMapFromUrl(imageUrl.toString())
        }
        sendNotification(remoteMessage.notification?.title ?: title,remoteMessage.notification?.body ?: message,remoteMessage,bitmap)

    }
    private fun getBitMapFromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            null
        }
    }


    override fun onNewToken(token: String) {
        Log.e(TAG, "Refreshed token: $token")
        UseCaseImpl().saveFireBaseToken(token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String) {

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun sendNotification(
        title: String?,
        body: String?,
        remoteMessage: RemoteMessage,
        bitmap: Bitmap?
    ) {
        val intent = Intent(this, SplashActivity::class.java)
        for ((key, value) in remoteMessage.data) {
            intent.putExtra(key, value)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getActivity(this, 0 /* Request code */, intent,
                FLAG_IMMUTABLE)
        } else {
            getActivity(this, 0 /* Request code */, intent,
                FLAG_ONE_SHOT)
        }

        val channelId = "RoseUserChannel963"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        if (bitmap != null) {
            notificationBuilder.setLargeIcon(bitmap)
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
            ) /*Notification with Image*/
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private val TAG = "FIREBAE"
    }
}