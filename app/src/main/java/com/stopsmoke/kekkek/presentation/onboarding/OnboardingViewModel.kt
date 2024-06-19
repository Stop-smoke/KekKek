package com.stopsmoke.kekkek.presentation.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.data.mapper.emptyHistory
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.UserConfig
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uid = MutableStateFlow("")
    val uid = _uid.asStateFlow()

    fun updateUid(uid: String) {
        viewModelScope.launch {
            _uid.emit(uid)
        }
    }

    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    fun updateUserName(name: String) {
        viewModelScope.launch {
            _userName.emit(name)
        }
    }


    private val _dailyCigarettePacks: MutableStateFlow<Int> =
        MutableStateFlow(0) // 하루에 담배를 몇 개비 정도 피우시나요?
    val dailyCigarettePacks = _dailyCigarettePacks.asStateFlow()

    fun updateDailyCigarettePacks(day: Int) {
        viewModelScope.launch {
            _dailyCigarettePacks.emit(day)
        }
    }

    private val _cigarettesPerPack = MutableStateFlow(0) // 담배 한 팩에 몇 개비가 들어있나요?
    val cigarettesPerPack = _cigarettesPerPack.asStateFlow()

    fun updateCigarettesPerPack(perPack: Int) {
        viewModelScope.launch {
            _cigarettesPerPack.emit(perPack)
        }
    }

    private val _cigarettePricePerPack = MutableStateFlow(0) // 담배 한 팩 당 가격이 얼마인가요?
    val cigarettePricePerPack = _cigarettePricePerPack.asStateFlow()

    fun updateCigarettePricePerPack(price: Int) {
        viewModelScope.launch {
            _cigarettePricePerPack.emit(price)
        }
    }

    private val _userBirthDate = MutableStateFlow<LocalDateTime?>(null) // 유저 태어난 년도
    val userBirthDate = _userBirthDate.asStateFlow()

    fun updateUserBirthDate(date: LocalDateTime) {
        viewModelScope.launch {
            _userBirthDate.emit(date)
        }
    }

    private val _onboardingUiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Loading)
    val onboardingUiState get() = _onboardingUiState.asStateFlow()

    fun updateUserData() {
        viewModelScope.launch {
            if (uid.value.isBlank()) {
                Log.e("OnboardingViewModel", "uid 값이 공백입니다")
                _onboardingUiState.emit(OnboardingUiState.LoadFail)
                return@launch
            }

//            if (userBirthDate.value == null) {
//                Log.e("OnboardingViewModel", "userBirthYear 값이 null 입니다")
//                _onboardingUiState.emit(OnboardingUiState.LoadFail)
//                return@launch
//            }

            val user = User.Registered(
                uid = uid.value,
                name = userName.value,
                location = null,
                profileImage = ProfileImage.Default,
                ranking = Long.MAX_VALUE,
                userConfig = UserConfig(
                    dailyCigarettesSmoked = dailyCigarettePacks.value,
                    packCigaretteCount = cigarettesPerPack.value,
                    packPrice = cigarettePricePerPack.value,
                    birthDate = LocalDateTime.now()
                ),
                history = emptyHistory()
            )
            userRepository.setUserData(user)
            userRepository.setOnboardingComplete(true)
            _onboardingUiState.emit(OnboardingUiState.Success)
        }
    }

    private val _nameDuplicationInspectionResult = MutableStateFlow<Boolean?>(null)
    val nameDuplicationInspectionResult: StateFlow<Boolean?> get() = _nameDuplicationInspectionResult

    fun nameDuplicateInspection(name: String) = viewModelScope.launch {
        val nameDuplicationInspectionResult = userRepository.nameDuplicateInspection(name)
        _nameDuplicationInspectionResult.emit(nameDuplicationInspectionResult)
    }

    fun setNameDuplicationInspectionResult(setBool: Boolean?) = viewModelScope.launch {
        _nameDuplicationInspectionResult.emit(setBool)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val isRegisteredUser: Flow<AuthenticationUiState> =
        userRepository.getUserData().flatMapLatest { user ->
            when (user) {
                is User.Error -> AuthenticationUiState.Error(user.t)
                is User.Guest -> AuthenticationUiState.Guest
                is User.Registered -> {
                    if (user.name.isBlank()) {
                        return@flatMapLatest flowOf(AuthenticationUiState.NewMember)
                    }
                    userRepository.setOnboardingComplete(true)
                    AuthenticationUiState.AlreadyUser
                }
            }
                .let {
                    flowOf(it)
                }
        }
}

sealed interface AuthenticationUiState {

    data object AlreadyUser : AuthenticationUiState

    data object NewMember : AuthenticationUiState

    data object Init : AuthenticationUiState

    data class Error(val t: Throwable?) : AuthenticationUiState

    data object Guest : AuthenticationUiState
}