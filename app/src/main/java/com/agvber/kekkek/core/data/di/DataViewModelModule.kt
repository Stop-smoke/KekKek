package com.agvber.kekkek.core.data.di

import com.agvber.kekkek.core.data.repository.AchievementRepositoryImpl
import com.agvber.kekkek.core.data.repository.BookmarkRepositoryImpl
import com.agvber.kekkek.core.data.repository.CommentRepositoryImpl
import com.agvber.kekkek.core.data.repository.NotificationRepositoryImpl
import com.agvber.kekkek.core.data.repository.PostRepositoryImpl
import com.agvber.kekkek.core.data.repository.ReplyRepositoryImpl
import com.agvber.kekkek.core.data.repository.SearchRepositoryImpl
import com.agvber.kekkek.core.domain.repository.AchievementRepository
import com.agvber.kekkek.core.domain.repository.BookmarkRepository
import com.agvber.kekkek.core.domain.repository.CommentRepository
import com.agvber.kekkek.core.domain.repository.NotificationRepository
import com.agvber.kekkek.core.domain.repository.PostRepository
import com.agvber.kekkek.core.domain.repository.ReplyRepository
import com.agvber.kekkek.core.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface DataViewModelModule {

    @Binds
    fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl,
    ): NotificationRepository

    @Binds
    fun bindSearchRepository(
        searchRepositoryImpl: SearchRepositoryImpl,
    ): SearchRepository

    @Binds
    fun bindsPostRepository(
        postRepositoryImpl: PostRepositoryImpl,
    ): PostRepository

    @Binds
    fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl,
    ): CommentRepository

    @Binds
    fun bindAchievementRepository(
        achievementRepositoryImpl: AchievementRepositoryImpl,
    ): AchievementRepository

    @Binds
    fun bindBookmarkRepository(
        bookmarkRepository: BookmarkRepositoryImpl
    ): BookmarkRepository

    @Binds
    fun bindReplyRepository(
        replyRepositoryImpl: ReplyRepositoryImpl
    ): ReplyRepository
}