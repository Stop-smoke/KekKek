package com.agvber.kekkek.core.authorization.kakao

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

@AndroidEntryPoint
class KakaoLoginActivity : ComponentActivity() {

    @Inject
    lateinit var kakaoAuthorization: NewKakaoAuthorization

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { runKakaoLogin() }
    }

    private suspend fun runKakaoLogin() = runCatching {
        if (!isKakaoTalkLoginAvailable()) {
            return@runCatching loginKakaoWeb()
        }
        loginKakaoTalk()
    }
        .also { kakaoAuthorization.setResult(it) }
        .onSuccess { finish() }
        .onFailure {
            it.printStackTrace()
            finish()
        }

    private fun isKakaoTalkLoginAvailable(): Boolean =
        UserApiClient.instance.isKakaoTalkLoginAvailable(this)

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun loginKakaoWeb() = suspendCancellableCoroutine<OAuthToken> { continuation ->
        UserApiClient.instance.loginWithKakaoAccount(
            context = this,
            callback = callback { result ->
                result
                    .onSuccess { continuation.resume(it) { } }
                    .onFailure { continuation.resumeWithException(it) }
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun loginKakaoTalk() = suspendCancellableCoroutine<OAuthToken> { continuation ->
        UserApiClient.instance.loginWithKakaoTalk(
            context = this,
            callback = callback { result ->
                result
                    .onSuccess { continuation.resume(it) { } }
                    .onFailure { continuation.resumeWithException(it) }
            }
        )
    }

    private inline fun callback(
        crossinline oAuthToken: (Result<OAuthToken>) -> Unit
    ): (OAuthToken?, Throwable?) -> Unit = { token: OAuthToken?, error: Throwable? ->
        error?.let {
            Log.e(TAG, "카카오 로그인 실패")
            oAuthToken(Result.failure(it))
        } ?: token?.let {
            Log.d(TAG, "카카오 로그인 성공 token: $token")
            oAuthToken(Result.success(it))
        } ?: oAuthToken(Result.failure(IllegalStateException("Token is null")))
    }

    companion object {
        const val TAG = "KakaoLoginActivity"
    }
}
