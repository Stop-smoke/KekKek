package com.stopsmoke.kekkek.presentation.settings.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProfileSettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun updateNickname(nickname: String) {
        viewModelScope.launch {
            val registeredUser =
                (userRepository.getUserData().first() as? User.Registered) ?: return@launch
            userRepository.setUserData(registeredUser.copy(name = nickname))
        }
    }

    fun updateBirthDate(date: LocalDateTime) {
        viewModelScope.launch {
            val registeredUser =
                (userRepository.getUserData().first() as? User.Registered) ?: return@launch
            val userConfig =
                registeredUser.copy(userConfig = registeredUser.userConfig.copy(birthDate = date))
            userRepository.setUserData(userConfig)
        }
    }

    fun updateIntroduce(introduce: String) {
        viewModelScope.launch {
            val registeredUser =
                (userRepository.getUserData().first() as? User.Registered) ?: return@launch

            userRepository.setUserData(registeredUser.copy(introduction = introduce))
        }
    }

    fun logout() {
        userRepository.logout()
    }

    fun withdraw() {
        viewModelScope.launch {
            userRepository.withdraw()
        }
    }

    fun settingProfile(inputStream: InputStream) {
        userRepository.setProfileImage(inputStream,"default")
    }

    val user = userRepository.getUserData("default")
}