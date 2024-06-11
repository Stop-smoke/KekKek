package com.stopsmoke.kekkek.presentation.community

interface CommunityCallbackListener {

    fun navigateToUserProfile(uid: String)

    fun navigateToPost(communityWritingItem: CommunityWritingItem)


}