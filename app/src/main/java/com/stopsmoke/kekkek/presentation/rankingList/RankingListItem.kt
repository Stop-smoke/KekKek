package com.stopsmoke.kekkek.presentation.rankingList

import java.time.LocalDateTime

data class RankingListItem(
    val name: String,
    val time: LocalDateTime,
    val rank: Int,
    val profile: String
)