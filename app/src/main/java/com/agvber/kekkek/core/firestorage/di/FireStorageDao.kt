package com.agvber.kekkek.core.firestorage.di

import com.agvber.kekkek.core.firestorage.dao.StorageDao
import com.agvber.kekkek.core.firestorage.data.StorageDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FireStorageDao {

    @Binds
    fun bindProfileImageDao(
        storageDaoImpl: StorageDaoImpl,
    ): StorageDao
}