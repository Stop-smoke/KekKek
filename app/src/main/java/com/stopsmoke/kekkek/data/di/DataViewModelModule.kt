package com.stopsmoke.kekkek.data.di

import com.stopsmoke.kekkek.data.repository.AchievementRepositoryImpl
import com.stopsmoke.kekkek.data.repository.BookmarkRepositoryImpl
import com.stopsmoke.kekkek.data.repository.CommentRepositoryImpl
import com.stopsmoke.kekkek.data.repository.NotificationRepositoryImpl
import com.stopsmoke.kekkek.data.repository.PostRepositoryImpl
import com.stopsmoke.kekkek.data.repository.SearchRepositoryImpl
import com.stopsmoke.kekkek.domain.repository.AchievementRepository
import com.stopsmoke.kekkek.domain.repository.CommentRepository
import com.stopsmoke.kekkek.domain.repository.NotificationRepository
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.SearchRepository
import com.stopsmoke.kekkek.firestore.dao.BookmarkRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
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
}