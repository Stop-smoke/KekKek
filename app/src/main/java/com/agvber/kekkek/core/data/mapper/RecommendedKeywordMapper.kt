package com.agvber.kekkek.core.data.mapper

import com.agvber.kekkek.core.domain.model.RecommendedKeyword
import com.agvber.kekkek.core.firestore.model.RecommendedKeywordEntity

internal fun RecommendedKeywordEntity.asExternalModel(): RecommendedKeyword =
    RecommendedKeyword(this.keyword ?: "")