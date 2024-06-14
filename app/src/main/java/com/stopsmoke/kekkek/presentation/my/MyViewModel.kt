package com.stopsmoke.kekkek.presentation.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.common.asResult
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
    userRepository: UserRepository,
) : ViewModel() {

    val userData = userRepository.getUserData()

    val activities = userRepository.getActivities().asResult()
}
