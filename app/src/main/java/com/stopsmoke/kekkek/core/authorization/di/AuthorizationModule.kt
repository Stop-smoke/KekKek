package com.stopsmoke.kekkek.core.authorization.di

import com.stopsmoke.kekkek.core.authorization.SocialAuthorization
import com.stopsmoke.kekkek.core.authorization.google.NewGoogleAuthorization
import com.stopsmoke.kekkek.core.authorization.kakao.NewKakaoAuthorization
import com.stopsmoke.kekkek.core.authorization.model.SocialSdk
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthorizationModule {

    @Binds
    @Authorization(SocialSdk.KAKAO)
    abstract fun bindsKakaoAuthorization(
        newKakaoAuthorization: NewKakaoAuthorization
    ): SocialAuthorization

    @Binds
    @Authorization(SocialSdk.GOOGLE)
    abstract fun bindsGoogleAuthorization(
        newGoogleAuthorization: NewGoogleAuthorization
    ): SocialAuthorization
}

