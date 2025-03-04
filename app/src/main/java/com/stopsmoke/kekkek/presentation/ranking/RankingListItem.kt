package com.stopsmoke.kekkek.presentation.ranking

import android.os.Parcelable
import com.stopsmoke.kekkek.core.domain.model.ProfileImage
import com.stopsmoke.kekkek.core.domain.model.User
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class RankingListItem(
    val userID : String,
    val name: String,
    val startTime: LocalDateTime?,
    val profileImage: String?,
    val clearAchievementInt: Int,
    val introduction: String?
):Parcelable

fun User.toRankingListItem() = RankingListItem(
    userID = uid,
    name = name,
    startTime = if (history.historyTimeList.isNotEmpty()
        && history.historyTimeList.last().quitSmokingStopDateTime == null) history.historyTimeList.last().quitSmokingStartDateTime
    else null,
    profileImage = if(profileImage is ProfileImage.Web) (profileImage as ProfileImage.Web).url else null,
    clearAchievementInt = clearAchievementsList.size,
    introduction = introduction
)