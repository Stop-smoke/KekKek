package com.stopsmoke.kekkek.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Ranking
import com.stopsmoke.kekkek.domain.model.RankingCategory
import kotlinx.coroutines.flow.Flow

interface RankingRepository {

    /**
     * 사용자 랭킹 리스트로 반환
     */

    fun getRankingItems(category: RankingCategory): Result<Flow<PagingData<Ranking>>>

    /**
     * 내 랭킹 정보 가져오는 함수
     */

    fun getMyRankingInfo()
}