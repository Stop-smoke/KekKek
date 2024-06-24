package com.stopsmoke.kekkek.firemseeage

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.stopsmoke.kekkek.firebaseauth.AuthenticationDataSource
import com.stopsmoke.kekkek.firestore.dao.UserDao
import com.stopsmoke.kekkek.notifications.commentNotificationNotify
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

    @Inject
    lateinit var authenticationDataSource: AuthenticationDataSource

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
            try {
                val uid = authenticationDataSource.getUid().firstOrNull() ?: return@launch
                userDao.updateUser(uid, mapOf("fcm_token" to token))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.d("FCM_TOKEN", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title
        val body = message.notification?.body
        commentNotificationNotify(title, body)
        Log.d("onMessageReceived", "title: $title body: $body")
    }
}