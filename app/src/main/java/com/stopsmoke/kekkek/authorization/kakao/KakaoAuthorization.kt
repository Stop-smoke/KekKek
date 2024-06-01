package com.stopsmoke.kekkek.authorization.kakao

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

class KakaoAuthorization {

    private var callback: KakaoAuthorizationCallbackListener? = null

    fun loginKakao(context: Context) {
        try {
            if (!context.getKakaoAppLoginAvailable()) {
                context.loginKakaoWeb()
                return
            }

            UserApiClient.instance.loginWithKakaoTalk(context) { token: OAuthToken?, error: Throwable? ->
                if (error != null || token == null) {
                    Log.e("kakao", "카카오톡으로 로그인 실패", error)
                    context.loginKakaoWeb()
                    return@loginWithKakaoTalk
                }

                UserApiClient.instance.me { user, error ->
                    if (error != null || user == null) {
                        callback?.onFailure(error)
                        Log.e("kakaouser", "사용자 정보 요청 실패", error)
                        return@me
                    }
                    callback?.onSuccess(token, user)
                    showSuccessLog(user)
                }
                Log.i("kakao", "카카오톡으로 로그인 성공 ${token.accessToken}")
            }
        } catch (e: Exception) {
            callback?.onFailure(e)
            e.printStackTrace()
        }
    }

    private fun Context.getKakaoAppLoginAvailable() =
        UserApiClient.instance.isKakaoTalkLoginAvailable(this)

    private fun Context.loginKakaoWeb() {
        UserApiClient.instance.loginWithKakaoAccount(
            context = this,
            callback = { token, error ->
                if (error != null || token == null) {
                    callback?.onFailure(error)
                    Log.e("kakao", "카카오계정으로 로그인 실패", error)
                    return@loginWithKakaoAccount
                }

                UserApiClient.instance.me { user, error ->
                    if (error != null || user == null) {
                        callback?.onFailure(error)
                        Log.e("kakaouser", "사용자 정보 요청 실패", error)
                        return@me
                    }
                    callback?.onSuccess(token, user)
                    showSuccessLog(user)
                }
                Log.i("kakao", "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        )
    }

    private fun showSuccessLog(
        user: User,
    ) {
        Log.i(
            "kakaouser", "사용자 정보 요청 성공" +
                    "\n회원번호: ${user.id}" +
                    "\n이메일: ${user.kakaoAccount?.email}" +
                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
        )
    }

    fun registerCallbackListener(callback: KakaoAuthorizationCallbackListener) {
        this.callback = callback
    }

    fun unregisterCallbackListener() {
        callback = null
    }
}