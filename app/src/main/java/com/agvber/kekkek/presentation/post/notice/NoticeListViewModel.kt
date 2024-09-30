package com.agvber.kekkek.presentation.post.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.agvber.kekkek.common.asResult
import com.agvber.kekkek.core.domain.model.PostCategory
import com.agvber.kekkek.core.domain.repository.PostRepository
import com.agvber.kekkek.presentation.community.toCommunityWritingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel @Inject constructor(
    postRepository: PostRepository,
) : ViewModel() {

    val noticePosts =
        postRepository.getPost(category = PostCategory.NOTICE)
            .map { pagingData ->
                pagingData.map { post ->
                    post.toCommunityWritingListItem()
                }
            }
            .cachedIn(viewModelScope)
            .asResult()
}

