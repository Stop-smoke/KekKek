package com.stopsmoke.kekkek.firestore.di

import com.stopsmoke.kekkek.firestore.dao.PostDao
import com.stopsmoke.kekkek.firestore.data.PostDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FireStoreDaoModule {

    @Binds
    fun bindPostDao(
        postDaoImpl: PostDaoImpl
    ): PostDao
}