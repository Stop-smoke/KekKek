package com.stopsmoke.kekkek.domain.model

sealed interface RankingCategory {
    data class Region(val regionName: String) : RankingCategory

    data object NATIONWIDE : RankingCategory

    data object Achievement : RankingCategory
}