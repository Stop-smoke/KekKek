package com.stopsmoke.kekkek.presentation.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    userRepository: UserRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<MyUiState> = MutableStateFlow(MyUiState.init())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    val userData = userRepository.getUserData("테스트_계정")

    fun updateUserData(user : User) = viewModelScope.launch {
        _uiState.update {
            MyUiState(
                myLoginUiState = MyLoginStatusState.LoggedUiState.MyIdLoggedUiState(
                    myItem = MyItem(
                        name = user.name,
                        rank = user.ranking.toInt(),
                        profileImg = when(user.profileImage){
                            is ProfileImage.Default -> ""
                            is ProfileImage.Web -> (user.profileImage as ProfileImage.Web).url
                        },
                        myWriting = MyWritingNum(
                            user.age ?: 0,
                            user.age ?: 0,
                            user.age ?: 0
                        ),
                        achievementNum = user.age ?: 0,
                        id = user.uid // id로 내가 쓴 글, 업적 데이터 조회?
                    )
                )
            )
        }
    }
}
