package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.core.domain.model.Written
import com.stopsmoke.kekkek.core.firestore.model.WrittenEntity

internal fun WrittenEntity?.asExternalModel(): Written =
    Written(
        uid = this?.uid ?: "null",
        name = this?.name ?: "null",
        profileImage = this?.profileImage?.let {
            ProfileImage.Web(
                it
            )
        }
            ?: ProfileImage.Default,
        ranking = this?.ranking ?: Long.MAX_VALUE
    )