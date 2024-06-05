package com.stopsmoke.kekkek.data.mapper

import com.google.firebase.Timestamp
import com.stopsmoke.kekkek.domain.model.Location
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.firestore.model.LocationEntity
import com.stopsmoke.kekkek.firestore.model.UserEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

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
        fcmToken = fcmToken,
        introduction = introduction,
        ranking = ranking,
        postBookmark = post_bookmark ?: emptyList(),
        postLike = post_like ?: emptyList(),
        startTime = start_time?.toLocalDateTime()
    )

internal fun LocationEntity.toExternalModel() = Location(
    latitude = latitude ?: 0,
    longitude = longitude ?: 0,
    region = region ?: ""
)

internal fun Timestamp.toLocalDateTime(): LocalDateTime = LocalDateTime.ofEpochSecond(this.seconds, this.nanoseconds, ZoneOffset.UTC)