package com.stopsmoke.kekkek.core.firemseeage.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseMessagingModule {

    @Provides
    fun provideFirebaseMessage(): FirebaseMessaging = Firebase.messaging
}