package com.agvber.kekkek

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agvber.kekkek.core.domain.repository.UserRepository
import com.agvber.kekkek.MainActivity
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import javax.inject.Inject
import kotlin.properties.Delegates

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    private var isOnboardingComplete by Delegates.notNull<Boolean>()

    @OptIn(ObsoleteCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        workSplash()
    }

    private fun workSplash() {
        val minimumDelayFlow = flow { delay(MINIMUM_SPLASH_DELAY); emit(Unit) }
        val initLoadFlow = flow { initLoadData(); emit(Unit) }

        initLoadFlow.zip(minimumDelayFlow) { _, _ -> }
            .launchIn(lifecycleScope)
            .invokeOnCompletion { startMainActivity() }
    }

    private suspend fun initLoadData() = runCatching {
        KakaoSdk.init(application.applicationContext, BuildConfig.KAKAO_NATIVE_API_KEY)
        Log.d(TAG, "KeyHash ${KakaoSdk.keyHash}")

        isOnboardingComplete = userRepository.isOnboardingComplete().first()
        userRepository.getUserData().first()
    }
        .onFailure { it.printStackTrace() }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(MainActivity.Companion.IS_ONBOARDING_COMPLETE_PARAM, isOnboardingComplete)
        }
        startActivity(intent)
    }

    companion object {
        const val TAG = "SplashActivity"
        private const val MINIMUM_SPLASH_DELAY = 800L
    }
}