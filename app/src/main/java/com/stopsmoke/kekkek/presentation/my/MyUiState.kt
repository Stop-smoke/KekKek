package com.stopsmoke.kekkek.presentation.my

data class MyUiState(
    val myLoginUiState: MyLoginStatusState
) {
    companion object{
        fun init() = MyUiState(myLoginUiState = MyLoginStatusState.NeedLoginUiState)
    }
}

sealed interface MyLoginStatusState {
    data object NeedLoginUiState: MyLoginStatusState

    sealed class LoggedUiState : MyLoginStatusState { // 게스트 로그인 기능 추가 경우? 갑자기 생각
        data class MyIdLoggedUiState(val myItem: MyItem): LoggedUiState()
        //data class GuestIdLoggedUiState(val guestItem: MyItem)
    }
}