package com.stopsmoke.kekkek.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.common.exception.GuestModeException
import com.stopsmoke.kekkek.core.domain.usecase.CheckNicknameUseCase
import com.stopsmoke.kekkek.core.domain.usecase.FinishOnboardingUseCase
import com.stopsmoke.kekkek.core.domain.usecase.GetUserDataUseCase
import com.stopsmoke.kekkek.core.domain.usecase.SignUpUseCase
import com.stopsmoke.kekkek.presentation.onboarding.model.AuthenticationUiState
import com.stopsmoke.kekkek.presentation.onboarding.model.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sinUpUseCase: SignUpUseCase,
    private val finishOnboardingUseCase: FinishOnboardingUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val checkNicknameUseCase: CheckNicknameUseCase,
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

    private val _onboardingUiState = MutableStateFlow<OnboardingUiState>(OnboardingUiState.Loading)
    val onboardingUiState = _onboardingUiState.asStateFlow()

    fun updateUserData() {
        viewModelScope.launch {
            try {
                sinUpUseCase(
                    uid = uid.value,
                    name = userName.value,
                    dailyCigarettesSmoked = dailyCigarettePacks.value,
                    packCigaretteCount = cigarettesPerPack.value,
                    packPrice = cigarettePricePerPack.value,
                )
                finishOnboardingUseCase()
                delay(1300)
                _onboardingUiState.emit(OnboardingUiState.Success)
            } catch (e: Exception) {
                _onboardingUiState.emit(OnboardingUiState.LoadFail(e))
                e.printStackTrace()
            }
        }
    }

    private val _nameDuplicationInspectionResult = MutableStateFlow<Boolean?>(null)
    val nameDuplicationInspectionResult: StateFlow<Boolean?> get() = _nameDuplicationInspectionResult

    fun nameDuplicateInspection(name: String) = viewModelScope.launch {
        val nameDuplicationInspectionResult = checkNicknameUseCase(name)
        _nameDuplicationInspectionResult.emit(nameDuplicationInspectionResult)
    }

    fun setNameDuplicationInspectionResult(setBool: Boolean?) = viewModelScope.launch {
        _nameDuplicationInspectionResult.emit(setBool)
    }

    private val registerEventListener = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val authenticationUiState: SharedFlow<AuthenticationUiState> =
        registerEventListener.flatMapLatest {
            if (uid.value.isBlank()) {
                return@flatMapLatest flowOf(AuthenticationUiState.Error(NullPointerException()))
            }

            getUserDataUseCase().mapLatest { user ->
                if (user.uid.isBlank()) {
                    return@mapLatest AuthenticationUiState.NewMember
                }
                finishOnboardingUseCase()
                AuthenticationUiState.AlreadyUser
            }
        }
            .catch {
                if (it is GuestModeException) {
                    emit(AuthenticationUiState.Guest)
                    return@catch
                }
                emit(AuthenticationUiState.Error(it))
            }
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000)
            )

    fun registeredApp() {
        viewModelScope.launch {
            registerEventListener.emit(Unit)
        }
    }
}