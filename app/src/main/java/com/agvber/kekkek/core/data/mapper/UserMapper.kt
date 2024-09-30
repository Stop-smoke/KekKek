package com.agvber.kekkek.core.data.mapper

import com.agvber.kekkek.core.domain.model.Location
import com.agvber.kekkek.core.domain.model.ProfileImage
import com.agvber.kekkek.core.domain.model.User
import com.agvber.kekkek.core.domain.model.UserConfig
import com.agvber.kekkek.core.firestore.model.LocationEntity
import com.agvber.kekkek.core.firestore.model.UserConfigEntity
import com.agvber.kekkek.core.firestore.model.UserEntity
import java.time.LocalDateTime

internal fun User.Registered.toEntity(): UserEntity =
    UserEntity(
        uid = uid,
        name = name,
        location = location?.run { LocationEntity(latitude, longitude, region) },
        profileImageUrl = (profileImage as? ProfileImage.Web)?.url,
        userConfig = UserConfigEntity(
            dailyCigarettesSmoked = userConfig.dailyCigarettesSmoked,
            packCigaretteCount = userConfig.packCigaretteCount,
            packPrice = userConfig.packPrice,
            birthDate = userConfig.birthDate.toFirebaseTimestamp()
        ),
        clearAchievementsList = clearAchievementsList,
        commentMy = commentMy,
        postMy = postMy,
        history = history.toEntity(),
        cigaretteAddictionTestResult = cigaretteAddictionTestResult,
        introduction = introduction
    )

internal fun UserEntity.toExternalModel(): User.Registered =
    User.Registered(
        uid = uid ?: "",
        name = name ?: "",
        location = location?.toExternalModel(),
        profileImage = if (profileImageUrl != null) ProfileImage.Web(url = profileImageUrl!!) else ProfileImage.Default,
        nickname = nickname,
        gender = gender,
        age = age,
        phoneNumber = phoneNumber,
        introduction = introduction,
        ranking = ranking ?: 0,
        postBookmark = postBookmark ?: emptyList(),
        postLike = postLike ?: emptyList(),
        startTime = start_time?.toLocalDateTime(),
        userConfig = UserConfig(
            dailyCigarettesSmoked = userConfig?.dailyCigarettesSmoked ?: 0,
            packCigaretteCount = userConfig?.packCigaretteCount ?: 0,
            packPrice = userConfig?.packPrice ?: 0,
            birthDate = userConfig?.birthDate?.toLocalDateTime() ?: LocalDateTime.now()
        ),
        clearAchievementsList = clearAchievementsList ?: emptyList(),
        commentMy = commentMy ?: emptyList(),
        postMy = postMy ?: emptyList(),
        history = history?.asExternalModel() ?: emptyHistory(),
        cigaretteAddictionTestResult = cigaretteAddictionTestResult
    )

internal fun LocationEntity.toExternalModel() = Location(
    latitude = latitude ?: 0,
    longitude = longitude ?: 0,
    region = region ?: ""
)