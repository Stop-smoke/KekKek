package com.stopsmoke.kekkek.core.authorization

import com.stopsmoke.kekkek.core.authorization.model.AuthorizationToken

interface SocialAuthorization {
    suspend fun login(): AuthorizationToken
}