package com.agvber.kekkek

import android.app.Application
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_API_KEY)
        Log.d(TAG, "KeyHash ${KakaoSdk.keyHash}")
    }

    companion object {
        const val TAG: String = "MainApplication"
    }
}