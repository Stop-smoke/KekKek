package com.stopsmoke.kekkek.core.firestore.di

import com.stopsmoke.kekkek.core.firestore.dao.AchievementDao
import com.stopsmoke.kekkek.core.firestore.dao.CommentDao
import com.stopsmoke.kekkek.core.firestore.dao.NotificationDao
import com.stopsmoke.kekkek.core.firestore.dao.PostDao
import com.stopsmoke.kekkek.core.firestore.dao.RankingDao
import com.stopsmoke.kekkek.core.firestore.dao.ReplyDao
import com.stopsmoke.kekkek.core.firestore.dao.SearchDao
import com.stopsmoke.kekkek.core.firestore.dao.UserDao
import com.stopsmoke.kekkek.core.firestore.data.AchievementDaoImpl
import com.stopsmoke.kekkek.core.firestore.data.CommentDaoImpl
import com.stopsmoke.kekkek.core.firestore.data.NotificationDaoImpl
import com.stopsmoke.kekkek.core.firestore.data.PostDaoImpl
import com.stopsmoke.kekkek.core.firestore.data.RankingDaoImpl
import com.stopsmoke.kekkek.core.firestore.data.ReplyDaoImpl
import com.stopsmoke.kekkek.core.firestore.data.SearchDaoImpl
import com.stopsmoke.kekkek.core.firestore.data.UserDaoImpl
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

    @Binds
    fun bindUserDao(
        userDaoImpl: UserDaoImpl,
    ): UserDao

    @Binds
    fun bindNotificationDao(
        notificationDaoImpl: NotificationDaoImpl,
    ): NotificationDao

    @Binds
    fun bindSearchDao(
        searchDaoImpl: SearchDaoImpl,
    ): SearchDao

    @Binds
    fun bindAchievementDao(
        achievementDaoImpl: AchievementDaoImpl,
    ): AchievementDao

    @Binds
    fun bindReplyDao(
        replyDaoImpl: ReplyDaoImpl
    ): ReplyDao
}