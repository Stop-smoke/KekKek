package com.agvber.kekkek.core.domain.repository

interface AuthenticationRepository {
    suspend fun loginKakao()
    suspend fun loginGoogle()
    suspend fun withdraw()
}