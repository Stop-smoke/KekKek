package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.ProfileImage
import com.stopsmoke.kekkek.domain.model.Written
import com.stopsmoke.kekkek.firestore.model.WrittenEntity

internal fun WrittenEntity?.asExternalModel(): Written =
    Written(
        uid = this?.uid ?: "null",
        name = this?.name ?: "null",
        profileImage = this?.profileImage?.let { ProfileImage.Web(it) }
            ?: ProfileImage.Default,
        ranking = this?.ranking ?: Long.MAX_VALUE
    )