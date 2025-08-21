package com.stopsmoke.kekkek.presentation.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.common.exception.GuestModeException
import com.stopsmoke.kekkek.core.domain.exception.UnRegisteredUserException
import com.stopsmoke.kekkek.core.domain.usecase.auth.LoginGoogleUseCase
import com.stopsmoke.kekkek.core.domain.usecase.auth.LoginKakaoUseCase
import com.stopsmoke.kekkek.presentation.onboarding.model.AuthenticationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val loginKakaoUseCase: LoginKakaoUseCase,
    private val loginGoogleUseCase: LoginGoogleUseCase
) : ViewModel() {

    private val _event: MutableSharedFlow<AuthenticationEvent> =
        MutableSharedFlow(replay = 1)
    val event: SharedFlow<AuthenticationEvent> = _event

    private var isRegisteredAppProcess = false

    fun loginKakao() = viewModelScope.launch {
        if (isRegisteredAppProcess) return@launch
        isRegisteredAppProcess = true

        runCatching { loginKakaoUseCase() }
            .onSuccess { _event.emit(AuthenticationEvent.AlreadyUser) }
            .onFailure { handleLoginFailure(it) }
    }
        .invokeOnCompletion { isRegisteredAppProcess = false }

    fun loginGoogle() = viewModelScope.launch {
        if (isRegisteredAppProcess) return@launch
        isRegisteredAppProcess = true

        runCatching { loginGoogleUseCase() }
            .onSuccess { _event.emit(AuthenticationEvent.AlreadyUser) }
            .onFailure { handleLoginFailure(it) }
    }
        .invokeOnCompletion { isRegisteredAppProcess = false }

    private suspend fun handleLoginFailure(exception: Throwable) {
        exception.printStackTrace()
        when (exception) {
            is GuestModeException ->
                _event.emit(AuthenticationEvent.Guest)

            is UnRegisteredUserException ->
                _event.emit(AuthenticationEvent.NewMember)

            else ->
                _event.emit(AuthenticationEvent.Error(exception))
        }
    }
}