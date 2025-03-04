package com.stopsmoke.kekkek.presentation.settings.model

data class SettingsItem(
    val profileInfo: ProfileInfo?,
    val settingTitle: String?,
    val version: String?,
    val cardViewType: SettingsMultiViewEnum
)

data class ProfileInfo(
    val profileImg: Int,
    val userNickname: String,
    val userDateOfBirth: String,
    val userIntroduction: String?
)
