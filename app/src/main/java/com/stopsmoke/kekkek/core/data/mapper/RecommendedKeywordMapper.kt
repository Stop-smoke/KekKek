package com.stopsmoke.kekkek.core.data.mapper

import com.stopsmoke.kekkek.core.domain.model.RecommendedKeyword
import com.stopsmoke.kekkek.core.firestore.model.RecommendedKeywordEntity

internal fun RecommendedKeywordEntity.asExternalModel(): RecommendedKeyword =
    RecommendedKeyword(this.keyword ?: "")