package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.Location
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.firestore.model.LocationEntity
import com.stopsmoke.kekkek.firestore.model.UserEntity

internal fun User.toEntity(): UserEntity =
    UserEntity(
        uid = uid,
        name = name,
        location = location?.run { LocationEntity(latitude, longitude, region) },
        profileImageUrl = (profileImage as? ProfileImage.Web)?.url
    )

internal fun UserEntity.toExternalModel(): User =
    User(
        uid = uid ?: "",
        name = name ?: "",
        location = location?.toExternalModel() ?: null,
        profileImage = if (profileImageUrl != null) ProfileImage.Web(url = profileImageUrl!!) else ProfileImage.Default,
        nickname = nickname,
        gender = gender,
        age = age,
        phoneNumber = phoneNumber,
        fcmToken = fcmToken
    )

internal fun LocationEntity.toExternalModel() = Location(
    latitude = latitude ?: 0,
    longitude = longitude ?: 0,
    region = region ?: ""
)