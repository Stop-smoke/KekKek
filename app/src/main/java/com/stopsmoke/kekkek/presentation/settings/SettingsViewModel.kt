package com.stopsmoke.kekkek.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.settings.model.ProfileImageUploadUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val user: StateFlow<User?> = userRepository.getUserData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

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
            if (user is User.Registered) userRepository.setUserDataForName(user, name)
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

    private val _onboardingScreenRequest = MutableSharedFlow<Unit>()
    val onboardingScreenRequest = _onboardingScreenRequest.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            _onboardingScreenRequest.emit(Unit)
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            userRepository.withdraw()
            _onboardingScreenRequest.emit(Unit)
        }
    }

    private val _profileImageUploadUiState: MutableStateFlow<ProfileImageUploadUiState> =
        MutableStateFlow(ProfileImageUploadUiState.Init)
    val profileImageUploadUiState = _profileImageUploadUiState.asStateFlow()

    fun initProfileImageUploadUiState() {
        viewModelScope.launch {
            _profileImageUploadUiState.emit(ProfileImageUploadUiState.Init)
        }
    }

    fun settingProfile(inputStream: InputStream) {
        viewModelScope.launch {
            try {
                _profileImageUploadUiState.emit(ProfileImageUploadUiState.Progress)
                userRepository.setProfileImage(inputStream)
                _profileImageUploadUiState.emit(ProfileImageUploadUiState.Success)
            } catch (e: Exception) {
                _profileImageUploadUiState.emit(ProfileImageUploadUiState.Error(e))
            }
        }
    }
}