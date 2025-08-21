package com.agvber.kekkek.core.firestore

import com.google.firebase.firestore.Query

internal const val ACHIEVEMENT_COLLECTION = "achievement"
internal const val NOTIFICATION_COLLECTION = "notification"
internal const val POST_COLLECTION = "post"
internal const val COMMENT_COLLECTION = "comment"
internal const val REPLY_COLLECTION = "reply"
internal const val USER_COLLECTION = "user"
internal const val WITHDRAW_USER_COLLECTION = "withdraw_user"
internal const val RECOMMENDED_KEYWORD_COLLECTION = "recommended_keyword"


internal fun Query.whereNotNullEqualTo(field: String, value: Any?): Query {
    if (value == null) {
        return this
    }
    return whereEqualTo(field, value)
}