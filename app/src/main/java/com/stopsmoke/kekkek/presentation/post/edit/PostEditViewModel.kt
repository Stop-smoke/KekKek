package com.stopsmoke.kekkek.presentation.post.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.PostEdit
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.mapper.toPostCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class PostEditViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PostEditUiState> =
        MutableStateFlow(PostEditUiState.InitUiState)
    val uiState = _uiState.asStateFlow()

    private val _post = MutableStateFlow<Post?>(null)
    val post get() = _post.asStateFlow()

    private val _myText = MutableLiveData<String>()
    val myText: LiveData<String> = _myText

    fun setString(text: String) {
        _myText.value = text
    }

    fun addPost(post: PostEdit) {
        viewModelScope.launch {
            try {
                postRepository.addPost(post)
                _uiState.emit(PostEditUiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.emit(PostEditUiState.ErrorExit)
            }
        }
    }

    fun addPost(post: PostEdit, inputStream: InputStream) {
        viewModelScope.launch {
            try {
                postRepository.addPost(post, inputStream)
                _uiState.emit(PostEditUiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.emit(PostEditUiState.ErrorExit)
            }
        }
    }

    fun editPost(postEdit: PostEdit) {
        viewModelScope.launch {
            try {
                val editPost = post.value?.copy(
                    title = postEdit.title,
                    text = postEdit.text,
                    dateTime = postEdit.dateTime,
                    category = postEdit.category.toPostCategory()
                )

                editPost?.let { postRepository.editPost(editPost) }
                _uiState.emit(PostEditUiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.emit(PostEditUiState.ErrorExit)
            }
        }
    }

    fun editPost(postEdit: PostEdit, inputStream: InputStream) {
        viewModelScope.launch {
            try {
                val editPost = post.value?.copy(
                    title = postEdit.title,
                    text = postEdit.text,
                    dateTime = postEdit.dateTime,
                    category = postEdit.category.toPostCategory()
                )

                editPost?.let { postRepository.editPost(editPost, inputStream) }
                _uiState.emit(PostEditUiState.Success)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.emit(PostEditUiState.ErrorExit)
            }
        }
    }

    fun updatePostId(postId: String) = viewModelScope.launch {
        val post = postRepository.getPostForPostId(postId)
        _post.emit(post)
    }

    fun setLoading() {
        _uiState.value = PostEditUiState.LadingUiState
    }


    fun setPostImage(inputStream: InputStream) {
        viewModelScope.launch {
            try {
                postRepository.setImage(
                    inputStream = inputStream,
                    path = "post/${post.value!!.id}/post_image.jpeg"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}