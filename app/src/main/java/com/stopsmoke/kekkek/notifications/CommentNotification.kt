package com.stopsmoke.kekkek.notifications

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.stopsmoke.kekkek.R

internal const val COMMENT_NOTIFICATION_ID = "comment_notification_id"
internal const val COMMENT_NOTIFY_ID = 101

fun Context.commentNotificationNotify(
    title: String?,
    content: String?
) {
    createCommentNotificationChannel()
    with(NotificationManagerCompat.from(this)) {
        // notificationId is a unique int for each notification that you must define
        if (ActivityCompat.checkSelfPermission(
                /* context = */ this@commentNotificationNotify,
                /* permission = */ Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notify(COMMENT_NOTIFY_ID, commentNotificationBuilder(title, content))
    }

}

private fun Context.commentNotificationBuilder(
    title: String?,
    content: String?
): Notification {
    val builder = NotificationCompat.Builder(
        /* context = */ this,
        /* channelId = */ COMMENT_NOTIFICATION_ID
    )
        .setSmallIcon(R.drawable.ic_brand_logo_white)
        .setColor(getColor(R.color.primary_blue))
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    return builder.build()
}

private fun Context.createCommentNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = getString(R.string.comment_notification_channel_name)
        val description = "fcm_description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(COMMENT_NOTIFICATION_ID, name, importance)
//            channel.description = description

        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager =
            getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}