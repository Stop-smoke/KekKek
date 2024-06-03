package com.stopsmoke.kekkek.data.mapper

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