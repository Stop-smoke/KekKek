package com.stopsmoke.kekkek.presentation.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostWrite
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostWriteViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post get() = _post.asStateFlow()

    fun addPost(post: PostWrite) {
        viewModelScope.launch {
            postRepository.addPost(post)
        }
    }

    fun editPost(post: PostWrite) {
        viewModelScope.launch {
            postRepository.editPost(post)
        }
    }

    fun updatePostId(postId:String) = viewModelScope.launch{
        val post = postRepository.getPostForPostId(postId)
        _post.emit(post)
    }
}