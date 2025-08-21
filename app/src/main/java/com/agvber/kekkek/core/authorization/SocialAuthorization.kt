package com.agvber.kekkek.core.authorization

import com.agvber.kekkek.core.authorization.model.AuthorizationToken

interface SocialAuthorization {
    suspend fun login(): AuthorizationToken
}