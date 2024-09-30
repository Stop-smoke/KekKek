package com.agvber.kekkek.core.domain.model

import java.time.LocalDateTime

sealed interface User {

    data object Guest : User

    data class Error(val t: Throwable?) : User

    data class Registered(
        val uid: String,
        val name: String,
        val location: Location?,
        var profileImage: ProfileImage,
        val nickname: String? = null,
        val gender: String? = null,
        val age: Int? = null,
        var phoneNumber: String? = null,
        var introduction: String? = null,
        var ranking: Long,
        var postBookmark: List<String> = emptyList(),
        var postLike: List<String> = emptyList(),
        var postMy: List<String> = emptyList(),
        var commentMy: List<String> = emptyList(),
        var startTime: LocalDateTime? = null,
        val userConfig: UserConfig,
        var clearAchievementsList: List<String> = emptyList(),
        var history: History,
        val cigaretteAddictionTestResult: String? = null
    ) : User
}