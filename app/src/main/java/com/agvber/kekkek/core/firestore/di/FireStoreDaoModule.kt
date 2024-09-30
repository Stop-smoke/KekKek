package com.agvber.kekkek.core.firestore.di

import com.agvber.kekkek.core.firestore.dao.AchievementDao
import com.agvber.kekkek.core.firestore.dao.CommentDao
import com.agvber.kekkek.core.firestore.dao.NotificationDao
import com.agvber.kekkek.core.firestore.dao.PostDao
import com.agvber.kekkek.core.firestore.dao.RankingDao
import com.agvber.kekkek.core.firestore.dao.ReplyDao
import com.agvber.kekkek.core.firestore.dao.SearchDao
import com.agvber.kekkek.core.firestore.dao.UserDao
import com.agvber.kekkek.core.firestore.data.AchievementDaoImpl
import com.agvber.kekkek.core.firestore.data.CommentDaoImpl
import com.agvber.kekkek.core.firestore.data.NotificationDaoImpl
import com.agvber.kekkek.core.firestore.data.PostDaoImpl
import com.agvber.kekkek.core.firestore.data.RankingDaoImpl
import com.agvber.kekkek.core.firestore.data.ReplyDaoImpl
import com.agvber.kekkek.core.firestore.data.SearchDaoImpl
import com.agvber.kekkek.core.firestore.data.UserDaoImpl
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