package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.Reply
import com.stopsmoke.kekkek.firestore.model.ReplyEntity

internal fun ReplyEntity?.asExternalModel(): Reply =
    Reply(
        written = this?.written.asExternalModel(),
        likeUser = this?.likeUser ?: emptyList(),
        unlikeUser = this?.unlikeUser ?: emptyList(),
        dateTime = this?.dateTime.asExternalModel(),
        text = this?.text ?: "null"
    )