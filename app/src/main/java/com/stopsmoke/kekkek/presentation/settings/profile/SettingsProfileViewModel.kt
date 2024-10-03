package com.stopsmoke.kekkek.presentation.settings.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
        if (name.isBlank()) {
            _nameDuplicationInspectionResult.emit(EditNameState.Empty)
        } else {
            val nameDuplicationInspectionResult = userRepository.nameDuplicateInspection(name)
            _nameDuplicationInspectionResult.emit(if (nameDuplicationInspectionResult) EditNameState.Success else EditNameState.Duplication)
        }
    }

    fun setUserDataForName(name: String) = viewModelScope.launch {
        try {
            userRepository.setUserName(name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setNameDuplicationInspectionResult(editNameState: EditNameState) = viewModelScope.launch {
        _nameDuplicationInspectionResult.emit(editNameState)
    }

    //introduction
    fun setUserDataForIntroduction(introduction: String) =
        viewModelScope.launch {
            try {
                userRepository.setUserIntroduction(introduction)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
}