package com.stopsmoke.kekkek.core.domain.repository

interface AuthenticationRepository {
    suspend fun loginKakao()
    suspend fun loginGoogle()
}