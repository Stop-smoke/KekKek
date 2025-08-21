package com.agvber.kekkek.presentation.community

interface CommunityCallbackListener {

    fun navigateToUserProfile(uid: String)

    fun navigateToPost(postId: String){}
}