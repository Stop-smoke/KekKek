package com.stopsmoke.kekkek.core.data.repository

import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.oAuthCredential
import com.stopsmoke.kekkek.core.authorization.SocialAuthorization
import com.stopsmoke.kekkek.core.authorization.di.Authorization
import com.stopsmoke.kekkek.core.authorization.model.AuthorizationToken
import com.stopsmoke.kekkek.core.authorization.model.SocialSdk
import com.stopsmoke.kekkek.core.domain.exception.UnRegisteredUserException
import com.stopsmoke.kekkek.core.domain.repository.AuthenticationRepository
import com.stopsmoke.kekkek.core.firestore.dao.UserDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthenticationRepositoryImpl @Inject constructor(
    @Authorization(SocialSdk.KAKAO) private val kakaoAuthorization: SocialAuthorization,
    @Authorization(SocialSdk.GOOGLE) private val googleAuthorization: SocialAuthorization,
    private val userDao: UserDao
) : AuthenticationRepository {

    override suspend fun loginKakao() {
        val authorizationToken: AuthorizationToken = kakaoAuthorization.login()
        val credential: AuthCredential = oAuthCredential(KAKAO_PROVIDER_ID) {
            idToken = authorizationToken.idToken
            accessToken = authorizationToken.accessToken
        }
        val authResult: AuthResult = signInWithCredential(credential)
        userDao.getUser(authResult.user!!.uid).firstOrNull()?.uid
            ?: throw UnRegisteredUserException()
    }

    override suspend fun loginGoogle() {
        val authorizationToken: AuthorizationToken = googleAuthorization.login()
        val credential = GoogleAuthProvider.getCredential(authorizationToken.idToken, null)
        val authResult: AuthResult = signInWithCredential(credential)
        userDao.getUser(authResult.user!!.uid).firstOrNull()?.uid
            ?: throw UnRegisteredUserException()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun signInWithCredential(credential: AuthCredential) =
        suspendCancellableCoroutine<AuthResult> { continuation ->
            Firebase.auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    continuation.resume(result)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
                .addOnCanceledListener {
                    continuation.resumeWithException(CancellationException())
                }
        }

    companion object {
        private const val KAKAO_PROVIDER_ID = "oidc.kakao"
        private const val DEFAULT_NAME = "켁켁이"

        const val TAG = "AuthenticationRepositoryImpl"
    }
}