package com.stopsmoke.kekkek.data.mapper

import com.stopsmoke.kekkek.domain.model.RecommendedKeyword
import com.stopsmoke.kekkek.firestore.model.RecommendedKeywordEntity

internal fun RecommendedKeywordEntity.asExternalModel(): RecommendedKeyword =
    RecommendedKeyword(this.keyword ?: "")