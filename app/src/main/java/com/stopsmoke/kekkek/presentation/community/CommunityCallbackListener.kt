package com.stopsmoke.kekkek.presentation.community

interface CommunityCallbackListener {

    fun navigateToUserProfile(uid: String)

    fun navigateToPost(postId: String){}

    fun navigateToPost(postId: String, position: Int){}

}