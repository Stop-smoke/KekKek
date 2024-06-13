package com.stopsmoke.kekkek.presentation.noticeWritingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.community.PostInfo
import com.stopsmoke.kekkek.presentation.community.UserInfo
import com.stopsmoke.kekkek.presentation.community.toCommunityWritingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NoticeListViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    val noticePosts = postRepository.getPost(category = PostCategory.NOTICE).let {
        when (it) {
            is Result.Error -> {
                it.exception?.printStackTrace()
                emptyFlow()
            }

            is Result.Loading -> emptyFlow()
            is Result.Success -> it.data.map { pagingData ->
                pagingData.map { post ->
                    post.toCommunityWritingListItem()
                }
            }
        }
    }.cachedIn(viewModelScope)

}

