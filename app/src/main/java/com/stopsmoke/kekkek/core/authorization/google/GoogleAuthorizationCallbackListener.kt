package com.stopsmoke.kekkek.core.authorization.google

import com.google.firebase.auth.FirebaseUser

interface GoogleAuthorizationCallbackListener {

    fun onSuccess(user: FirebaseUser)

    fun onFailure(t: Throwable?)
}