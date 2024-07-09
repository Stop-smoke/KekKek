package com.stopsmoke.kekkek.presentation.post.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.community.CommunityWritingItem
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel @Inject constructor(
    postRepository: PostRepository,
) : ViewModel() {

    val noticePosts: Flow<PagingData<CommunityWritingItem>> =
        postRepository.getPost(category = PostCategory.NOTICE)
            .map { pagingData ->
                pagingData.map { post ->
                    post.toCommunityWritingListItem()
                }
            }
            .cachedIn(viewModelScope)
}

