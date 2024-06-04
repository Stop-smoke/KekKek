package com.stopsmoke.kekkek.storage.di

import com.stopsmoke.kekkek.storage.dao.UserDao
import com.stopsmoke.kekkek.storage.data.UserMemoryStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface StorageModule {

    @Binds
    fun bindUserDao(
        userMemoryStorage: UserMemoryStorage,
    ): UserDao

}