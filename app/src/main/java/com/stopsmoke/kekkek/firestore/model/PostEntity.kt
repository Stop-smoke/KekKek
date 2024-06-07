package com.stopsmoke.kekkek.firestore.model

import com.google.firebase.firestore.PropertyName

data class PostEntity(
    @get:PropertyName("id") @set:PropertyName("id")
    var id: String? = null,

    @get:PropertyName("comment_id") @set:PropertyName("comment_id")
    var commentId: String? = null,

    @get:PropertyName("written") @set:PropertyName("written")
    var written: Written? = null,

    @get:PropertyName("title") @set:PropertyName("title")
    var title: String? = null,

    @get:PropertyName("text") @set:PropertyName("text")
    var text: String? = null,

    @get:PropertyName("date_time") @set:PropertyName("date_time")
    var dateTime: DateTimeEntity? = null,

    @get:PropertyName("like_user") @set:PropertyName("like_user")
    var likeUser: List<String> = emptyList(),

    @get:PropertyName("unlike_user") @set:PropertyName("unlike_user")
    var unlikeUser: List<String> = emptyList(),

    @get:PropertyName("categories") @set:PropertyName("categories")
    var categories: String? = null,

    @get:PropertyName("views") @set:PropertyName("views")
    var views: Long? = null,

    @get:PropertyName("comment_user") @set:PropertyName("comment_user")
    var commentUser: List<String> = emptyList()
) {

    data class Written(
        @get:PropertyName("uid") @set:PropertyName("uid")
        var uid: String? = null,

        @get:PropertyName("name") @set:PropertyName("name")
        var name: String? = null,

        @get:PropertyName("profile_image") @set:PropertyName("profile_image")
        var profileImage: String? = null,

        @get:PropertyName("ranking") @set:PropertyName("ranking")
        var ranking: Long? = null,
    )

}