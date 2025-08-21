package com.stopsmoke.kekkek.core.authorization.kakao

import android.content.Context
import android.content.Intent
import com.kakao.sdk.auth.model.OAuthToken
import com.stopsmoke.kekkek.core.authorization.SocialAuthorization
import com.stopsmoke.kekkek.core.authorization.model.AuthorizationToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewKakaoAuthorization @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : SocialAuthorization {

    private val result: MutableSharedFlow<Result<AuthorizationToken>> = MutableSharedFlow(1)

    suspend fun setResult(oAuthToken: Result<OAuthToken>) = oAuthToken
        .onSuccess {
            if (it.idToken == null) {
                result.emit(TOKEN_NULL_ERROR)
                return@onSuccess
            }

            val token = AuthorizationToken(it.idToken!!, it.accessToken)
            result.emit(Result.success(token))
        }
        .onFailure {
            result.emit(Result.failure(it))
        }

    override suspend fun login(): AuthorizationToken {
        val intent = Intent(appContext, KakaoLoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
        return result.first().getOrThrow()
    }

    companion object {
        private val TOKEN_NULL_ERROR: Result<AuthorizationToken> =
            Result.failure(IllegalStateException("kakao idToken is null."))
    }
}