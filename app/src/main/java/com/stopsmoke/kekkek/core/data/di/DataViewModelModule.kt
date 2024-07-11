package com.stopsmoke.kekkek.core.data.di

import com.stopsmoke.kekkek.core.data.repository.AchievementRepositoryImpl
import com.stopsmoke.kekkek.core.data.repository.BookmarkRepositoryImpl
import com.stopsmoke.kekkek.core.data.repository.CommentRepositoryImpl
import com.stopsmoke.kekkek.core.data.repository.NotificationRepositoryImpl
import com.stopsmoke.kekkek.core.data.repository.PostRepositoryImpl
import com.stopsmoke.kekkek.core.data.repository.ReplyRepositoryImpl
import com.stopsmoke.kekkek.core.data.repository.SearchRepositoryImpl
import com.stopsmoke.kekkek.core.domain.repository.AchievementRepository
import com.stopsmoke.kekkek.core.domain.repository.BookmarkRepository
import com.stopsmoke.kekkek.core.domain.repository.CommentRepository
import com.stopsmoke.kekkek.core.domain.repository.NotificationRepository
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import com.stopsmoke.kekkek.core.domain.repository.ReplyRepository
import com.stopsmoke.kekkek.core.domain.repository.SearchRepository
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