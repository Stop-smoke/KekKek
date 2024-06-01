package com.stopsmoke.kekkek.authorization.kakao

import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.model.User

interface KakaoAuthorizationCallbackListener {

    fun onSuccess(token: OAuthToken, user: User)

    fun onFailure(t: Throwable?)
}