package com.stopsmoke.kekkek.presentation.achievement

import androidx.lifecycle.ViewModel
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.Achievement
import com.stopsmoke.kekkek.domain.repository.AchievementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    achievementRepository: AchievementRepository,
) : ViewModel() {

    val achievements: Flow<List<Achievement>> = achievementRepository.getAchievementItems()
        .let {
            when (it) {
                is Result.Error -> {
                    it.exception?.printStackTrace()
                    emptyFlow()
                }

                is Result.Loading -> emptyFlow()
                is Result.Success -> it.data
            }
        }
}