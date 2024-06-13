package com.stopsmoke.kekkek.data.mapper

import com.google.firebase.Timestamp
import com.stopsmoke.kekkek.domain.model.History
import com.stopsmoke.kekkek.domain.model.Location
import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.UserConfig
import com.stopsmoke.kekkek.firestore.model.LocationEntity
import com.stopsmoke.kekkek.firestore.model.UserConfigEntity
import com.stopsmoke.kekkek.firestore.model.UserEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

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
        cigaretteAddictionTestResult = cigaretteAddictionTestResult
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
        ranking = ranking ?: Long.MAX_VALUE,
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

internal fun Timestamp.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofEpochSecond(this.seconds, 0 , ZoneOffset.UTC)

fun LocalDateTime.toFirebaseTimestamp(): Timestamp {
    val milliseconds = this.toInstant(ZoneOffset.UTC).toEpochMilli()
    return Timestamp(milliseconds / 1000, ((milliseconds % 1000) * 1000000).toInt())
}