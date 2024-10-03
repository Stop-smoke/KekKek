package com.stopsmoke.kekkek.core.domain.model

import java.time.LocalDateTime

data class User(
    val uid: String,
    val name: String,
    var profileImage: ProfileImage,
    var phoneNumber: String? = null,
    var introduction: String? = null,
    var ranking: Long,
    var startTime: LocalDateTime? = null,
    val userConfig: UserConfig,
    var clearAchievementsList: List<String> = emptyList(),
    var history: History,
    val cigaretteAddictionTestResult: String? = null,
    val role: UserRole
)