package com.stopsmoke.kekkek.core.authorization

import com.google.firebase.auth.FirebaseUser

interface FirebaseAuthorizationEvent {

    fun onSuccess(user: FirebaseUser)

    fun onFailure(t: Throwable?)
}