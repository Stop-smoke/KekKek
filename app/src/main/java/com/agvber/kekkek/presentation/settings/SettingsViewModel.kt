package com.agvber.kekkek.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.repository.UserRepository
import com.agvber.kekkek.presentation.settings.model.ProfileImageUploadUiState
import com.agvber.kekkek.presentation.settings.profile.model.ExitAppUiState
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

    private val _exitAppUiState = MutableSharedFlow<ExitAppUiState>()
    val exitAppUiState = _exitAppUiState.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logout()
                _exitAppUiState.emit(ExitAppUiState.Logout)
            } catch (e: Exception) {
                _exitAppUiState.emit(ExitAppUiState.Failure(e))
                e.printStackTrace()
            }
        }
    }

    fun withdraw() {
        viewModelScope.launch {
            try {
                userRepository.withdraw()
                _exitAppUiState.emit(ExitAppUiState.Withdraw)
            } catch (e: Exception) {
                _exitAppUiState.emit(ExitAppUiState.Failure(e))
                e.printStackTrace()
            }
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
                e.printStackTrace()
                _profileImageUploadUiState.emit(ProfileImageUploadUiState.Error(e))
            }
        }
    }
}