package com.stopsmoke.kekkek.core.authorization.google

import android.content.Context
import android.content.Intent
import com.stopsmoke.kekkek.core.authorization.SocialAuthorization
import com.stopsmoke.kekkek.core.authorization.kakao.KakaoLoginActivity
import com.stopsmoke.kekkek.core.authorization.model.AuthorizationToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewGoogleAuthorization @Inject constructor(
    @ApplicationContext private val appContext: Context
) : SocialAuthorization {

    private val result: MutableSharedFlow<Result<AuthorizationToken>> = MutableSharedFlow(1)

    suspend fun setResult(oAuthToken: Result<AuthorizationToken>) {
        result.emit(oAuthToken)
    }

    override suspend fun login(): AuthorizationToken {
        val intent = Intent(appContext, GoogleLoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
        return result.first().getOrThrow()
    }

    companion object {
        const val TAG: String = "NewGoogleAuthorization"
    }
}