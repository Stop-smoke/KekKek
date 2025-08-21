package com.stopsmoke.kekkek

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_API_KEY)
        Log.d(TAG, "KeyHash ${KakaoSdk.keyHash}")

        Firebase.auth.currentUser?.providerData?.forEach {
            Log.d(TAG, it.providerId)
        }
    }

    companion object {
        const val TAG: String = "MainApplication"
    }
}