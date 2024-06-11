package com.stopsmoke.kekkek.presentation.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.presentation.community.PostInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 userRepository에 자주 정보를 가져오는 것보다는 sharedViewModle에 넣는게 좋지 않나? 시간 되면 바꿨으면 하는 생각 ㅎ
 postNoticeTitle도 하는 김에 넣고 ㅎㅎ...
 **/
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {
}
