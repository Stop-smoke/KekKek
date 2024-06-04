package com.stopsmoke.kekkek.firestore.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {

    @Provides
    fun provideFireStoreInstance(): FirebaseFirestore = Firebase.firestore
}