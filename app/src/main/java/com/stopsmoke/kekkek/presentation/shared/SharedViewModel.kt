package com.stopsmoke.kekkek.presentation.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.my.MyItem
import com.stopsmoke.kekkek.presentation.my.MyLoginStatusState
import com.stopsmoke.kekkek.presentation.my.MyUiState
import com.stopsmoke.kekkek.presentation.my.MyWritingNum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
userRepository에 자주 정보를 가져오는 것보다는 sharedViewModle에 넣는게 좋지 않나? 시간 되면 바꿨으면 하는 생각 ㅎ
postNoticeTitle도 하는 김에 넣고 ㅎㅎ...
 **/
@HiltViewModel
class SharedViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _noticeBanner = MutableStateFlow(Post.emptyPost())
    val noticeBanner: StateFlow<Post> get() = _noticeBanner.asStateFlow()


    private val _sharedState = MutableStateFlow(SharedState.init())
    val sharedState get() = _sharedState.asStateFlow()


    val userData = userRepository.getUserData("테스트_계정")

    init {
        viewModelScope.launch {
            val noticeBannerPost = postRepository.getTopNotice()
            _noticeBanner.emit(noticeBannerPost)
        }
    }

    fun updateUserData(user: User) = viewModelScope.launch {
        when(user) {
            is User.Registered -> {
                _sharedState.update { prev ->
                    prev.copy(
                        loginUiState = LoginStatusState.LoggedUiState.MyIdLoggedUiState(
                            myStateItem = MyStateItem(
                                name = user.name,
                                rank = user.ranking,
                                profileImg = when (user.profileImage) {
                                    is ProfileImage.Default -> ""
                                    is ProfileImage.Web -> (user.profileImage as ProfileImage.Web).url
                                },
                                myWriting = MyStateWritingNum(
                                    user.postMy.size ?: 0,
                                    user.commentMy.size ?: 0,
                                    user.postBookmark.size ?: 0
                                ),
                                achievementNum = user.clearAchievementsList.size ?: 0,
                                id = user.uid // id로 내가 쓴 글, 업적 데이터 조회?
                            )
                        )
                    )
                }
            }

            else -> _sharedState.update { prev->
                prev.copy(
                    loginUiState = LoginStatusState.NeedLoginUiState
                )
            }
        }
    }


}
