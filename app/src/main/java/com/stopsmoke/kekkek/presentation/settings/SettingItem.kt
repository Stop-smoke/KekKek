package com.stopsmoke.kekkek.presentation.settings

data class SettingItem(
    val profileInfo: ProfileInfo?,
    val settingTitle: String?,
    val version: String?,
    val cardViewType: MultiViewEnum
)

data class ProfileInfo(
    val profileImg: Int,
    val userNickname: String,
    val userDateOfBirth: String,
    val userIntroduction: String?
)
