package com.stopsmoke.kekkek.presentation.settings.profile

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
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class SettingsProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    val user: Flow<User> = userRepository.getUserData()

    private val _nameDuplicationInspectionResult =
        MutableStateFlow<EditNameState>(EditNameState.Empty)
    val nameDuplicationInspectionResult: StateFlow<EditNameState> get() = _nameDuplicationInspectionResult


    //name
    fun nameDuplicateInspection(name: String) = viewModelScope.launch {
        if (name.isNullOrBlank()) _nameDuplicationInspectionResult.emit(EditNameState.Empty)
        else {
            val nameDuplicationInspectionResult = userRepository.nameDuplicateInspection(name)
            _nameDuplicationInspectionResult.emit(if (nameDuplicationInspectionResult) EditNameState.Success else EditNameState.Duplication)
        }
    }

    fun setUserDataForName(name: String, onComplete: () -> Unit) = viewModelScope.launch {
        user.collect { user ->
            if (user is User.Registered) userRepository.setUserDataForName(user, name)
            onComplete()
        }
    }

    fun setNameDuplicationInspectionResult(editNameState: EditNameState) = viewModelScope.launch {
        _nameDuplicationInspectionResult.emit(editNameState)
    }


    //introduction
    fun setUserDataForIntroduction(introduction: String, onComplete: () -> Unit) =
        viewModelScope.launch {
            user.collect { user ->
                when (user) {
                    is User.Registered -> {
                        val newUserData = user.copy(introduction = introduction)
                        userRepository.setUserData(newUserData)
                        onComplete()
                    }

                    is User.Error -> {}
                    User.Guest -> {}
                }
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