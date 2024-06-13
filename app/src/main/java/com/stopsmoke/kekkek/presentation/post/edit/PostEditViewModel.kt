package com.stopsmoke.kekkek.presentation.post.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.PostWrite
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {

    fun editPost(post: PostWrite) {
        viewModelScope.launch {
            postRepository.editPost(post)
        }
    }

}