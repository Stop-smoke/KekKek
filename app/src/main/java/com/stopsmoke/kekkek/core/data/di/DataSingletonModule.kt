package com.stopsmoke.kekkek.core.data.di

import com.stopsmoke.kekkek.core.data.repository.UserRepositoryImpl
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSingletonModule {

    @Binds
    fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository
}