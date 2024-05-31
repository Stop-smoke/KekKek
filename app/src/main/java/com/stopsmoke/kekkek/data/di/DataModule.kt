package com.stopsmoke.kekkek.data.di

import com.stopsmoke.kekkek.data.repository.UserRepositoryImpl
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface DataModule {

    @Binds
    fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

}