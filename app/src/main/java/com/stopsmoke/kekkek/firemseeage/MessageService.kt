package com.stopsmoke.kekkek.firemseeage

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.firestore.dao.UserDao
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MessageService : FirebaseMessagingService() {

    @Inject
    lateinit var userDao: UserDao

    private lateinit var serviceLifeScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        serviceLifeScope = CoroutineScope(Dispatchers.Unconfined)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceLifeScope.cancel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        serviceLifeScope.launch {
            userDao.getUser().firstOrNull()?.let {
                userDao.setUser(it.copy(fcmToken = token))
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val builder = NotificationCompat.Builder(
            /* context = */ this@MessageService,
            /* channelId = */ getString(R.string.post_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_cigarette)
//            .setColor(AbleBlue.toArgb())
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    this@MessageService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(100, builder.build())
        }
    }
}