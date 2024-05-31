package com.stopsmoke.kekkek.firestore.di

import com.stopsmoke.kekkek.firestore.dao.CommentDao
import com.stopsmoke.kekkek.firestore.dao.PostDao
import com.stopsmoke.kekkek.firestore.dao.RankingDao
import com.stopsmoke.kekkek.firestore.data.CommentDaoImpl
import com.stopsmoke.kekkek.firestore.data.PostDaoImpl
import com.stopsmoke.kekkek.firestore.data.RankingDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FireStoreDaoModule {

    @Binds
    fun bindPostDao(
        postDaoImpl: PostDaoImpl,
    ): PostDao

    @Binds
    fun bindCommentDao(
        commentDaoImpl: CommentDaoImpl,
    ): CommentDao

    @Binds
    fun bindRankingDao(
        rankingDaoImpl: RankingDaoImpl,
    ): RankingDao
}