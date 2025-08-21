package com.agvber.kekkek.core.data.mapper

import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.model.UserConfig
import com.agvber.kekkek.core.domain.model.UserRole
import com.agvber.kekkek.core.firestore.model.UserConfigEntity
import com.agvber.kekkek.core.firestore.model.UserEntity
import java.time.LocalDateTime

internal fun User.toEntity(): UserEntity =
    UserEntity(
        uid = uid,
        name = name,
        profileImageUrl = (profileImage as? ProfileImage.Web)?.url,
        userConfig = UserConfigEntity(
            dailyCigarettesSmoked = userConfig.dailyCigarettesSmoked,
            packCigaretteCount = userConfig.packCigaretteCount,
            packPrice = userConfig.packPrice,
            birthDate = userConfig.birthDate.toFirebaseTimestamp()
        ),
        clearAchievementsList = clearAchievementsList,
        history = history.toEntity(),
        cigaretteAddictionTestResult = cigaretteAddictionTestResult,
        introduction = introduction
    )

internal fun UserEntity.toExternalModel(): User =
    User(
        uid = uid ?: "",
        name = name ?: "",
        profileImage = if (profileImageUrl != null) ProfileImage.Web(url = profileImageUrl!!) else ProfileImage.Default,
        phoneNumber = phoneNumber,
        introduction = introduction,
        ranking = ranking ?: 0,
        startTime = start_time?.toLocalDateTime(),
        userConfig = UserConfig(
            dailyCigarettesSmoked = userConfig?.dailyCigarettesSmoked ?: 0,
            packCigaretteCount = userConfig?.packCigaretteCount ?: 0,
            packPrice = userConfig?.packPrice ?: 0,
            birthDate = userConfig?.birthDate?.toLocalDateTime() ?: LocalDateTime.now()
        ),
        clearAchievementsList = clearAchievementsList ?: emptyList(),
        history = history?.asExternalModel() ?: emptyHistory(),
        cigaretteAddictionTestResult = cigaretteAddictionTestResult,
        role = UserRole.USER
    )