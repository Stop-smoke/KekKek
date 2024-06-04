package com.stopsmoke.kekkek.firestorage.di

import com.stopsmoke.kekkek.firestorage.dao.StorageDao
import com.stopsmoke.kekkek.firestorage.data.StorageDaoImpl
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