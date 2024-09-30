package com.agvber.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.agvber.kekkek.common.Result
import com.agvber.kekkek.core.domain.model.Ranking
import com.agvber.kekkek.core.domain.model.RankingCategory
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