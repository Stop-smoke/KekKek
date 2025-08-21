package com.agvber.kekkek.core.authorization.di

import com.agvber.kekkek.core.authorization.model.SocialSdk
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Authorization(val socialSdk: SocialSdk)