package com.stopsmoke.kekkek.presentation.my

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<MyUiState> = MutableStateFlow(MyUiState.init())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    val userData = userRepository.getUserData("테스트_계정").getOrNull()
}
