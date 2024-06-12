package com.stopsmoke.kekkek.presentation.shared

data class SharedState(
    val loginUiState: LoginStatusState
) {
    companion object {
        fun init() = SharedState(
            loginUiState = LoginStatusState.NeedLoginUiState
        )
    }
}

sealed interface LoginStatusState {
    data object NeedLoginUiState : LoginStatusState

    sealed class LoggedUiState : LoginStatusState { // 게스트 로그인 기능 추가 경우? 갑자기 생각
        data class MyIdLoggedUiState(val myStateItem: MyStateItem) : LoggedUiState()
        //data class GuestIdLoggedUiState(val guestItem: MyItem)
    }
}

data class MyStateItem(
    val name: String,
    val rank: Long,
    val profileImg: String,
    val myWriting: MyStateWritingNum,
    val achievementNum: Int,
    val id: String // id로 내가 쓴 글, 업적 데이터 조회?
)

data class MyStateWritingNum(
    val writing: Int,
    val comment: Int,
    val bookmark: Int
)