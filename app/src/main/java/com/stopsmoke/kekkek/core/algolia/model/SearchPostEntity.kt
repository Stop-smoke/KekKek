package com.stopsmoke.kekkek.core.algolia.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchPostEntity(
    @SerialName("id")
    var id: String? = null,

    @SerialName("comment_id")
    var commentId: String? = null,

    @SerialName("written")
    var written: WrittenEntity? = null,

    @SerialName("title")
    var title: String? = null,

    @SerialName("text")
    var text: String? = null,

    @SerialName("like_user")
    var likeUser: List<String> = emptyList(),

    @SerialName("unlike_user")
    var unlikeUser: List<String> = emptyList(),

    @SerialName("bookmark_user")
    var bookmarkUser: List<String> = emptyList(),

    @SerialName("category")
    var category: String? = null,

    @SerialName("views")
    var views: Long? = null,

    @SerialName("comment_count")
    var commentCount: Long? = null,

    @SerialName("date_time")
    var dateTime: DateTimeEntity? = null,

    @SerialName("images_url")
    val imagesUrl: List<String> = emptyList()
)