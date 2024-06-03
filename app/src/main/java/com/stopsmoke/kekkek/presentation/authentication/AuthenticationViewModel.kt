package com.stopsmoke.kekkek.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun updateUserData(user: User) = try {
        viewModelScope.launch {
            userRepository.setUserData(user)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}