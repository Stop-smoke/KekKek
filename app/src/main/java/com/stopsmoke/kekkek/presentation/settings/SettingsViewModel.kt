package com.stopsmoke.kekkek.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.ProfileImageUploadResult
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val user: Flow<User> = userRepository.getUserData()

    private val _nameDuplicationInspectionResult = MutableStateFlow<Boolean?>(null)
    val nameDuplicationInspectionResult: StateFlow<Boolean?> get() = _nameDuplicationInspectionResult


    fun nameDuplicateInspection(name: String) = viewModelScope.launch {
        if (name.isNullOrBlank()) _nameDuplicationInspectionResult.emit(null)
        else {
            val nameDuplicationInspectionResult = userRepository.nameDuplicateInspection(name)
            _nameDuplicationInspectionResult.emit(nameDuplicationInspectionResult)
        }
    }

    fun setNameDuplicationInspectionResult(setBool: Boolean?) = viewModelScope.launch {
        _nameDuplicationInspectionResult.emit(setBool)
    }

    fun setUserData(name: String) = viewModelScope.launch {
        user.collect { user ->
            if(user is User.Registered) userRepository.setUserDataForName(user, name)
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
        viewModelScope.launch {
            userRepository.setProfileImage(inputStream).collectLatest {
                when (it) {
                    is ProfileImageUploadResult.Error -> {}
                    is ProfileImageUploadResult.Progress -> {}
                    is ProfileImageUploadResult.Success -> {

                    }
                }
            }
        }
    }



}