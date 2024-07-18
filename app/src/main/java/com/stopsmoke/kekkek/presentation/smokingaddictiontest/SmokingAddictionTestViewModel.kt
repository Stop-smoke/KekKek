package com.stopsmoke.kekkek.presentation.smokingaddictiontest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.core.domain.usecase.UpdateCigaretteAddictionTestResultUseCase
import com.stopsmoke.kekkek.presentation.smokingaddictiontest.model.SmokingQuestionnaireUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmokingAddictionTestViewModel @Inject constructor(
    private val updateCigaretteAddictionTestResultUseCase: UpdateCigaretteAddictionTestResultUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<SmokingAddictionTestUiState> = MutableStateFlow(SmokingAddictionTestUiState.NormalUiState)
    val uiState: StateFlow<SmokingAddictionTestUiState> = _uiState.asStateFlow()

    private val _score = MutableStateFlow<Map<Int, Int>>(mapOf()) // pageIndex, result
    val score: StateFlow<Map<Int, Int>> = _score.asStateFlow()

    fun addScore(pageIndex: Int, score: Int) {
        viewModelScope.launch {
            val newScore = _score.value.toMutableMap()
            newScore[pageIndex] = score
            _score.emit(newScore)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val result: Flow<SmokingQuestionnaireUiState> = score.mapLatest { scoreList ->
        when (scoreList.values.sum()) {
            in 0..13 -> {
                SmokingQuestionnaireUiState.Low
            }

            in 14..19 -> {
                SmokingQuestionnaireUiState.Medium
            }

            else -> {
                SmokingQuestionnaireUiState.High
            }
        }
    }

    private val _pageIndex = MutableStateFlow(0)
    val pageIndex = _pageIndex.asStateFlow()

    fun updatePageIndex(index: Int) {
        viewModelScope.launch {
            _pageIndex.emit(index)
        }
    }

    fun updateCigaretteAddictionTestResult(result: String) {
        try {
            viewModelScope.launch {
                if (score.value.isNotEmpty()) {
                    updateCigaretteAddictionTestResultUseCase(result)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
            _uiState.value = SmokingAddictionTestUiState.ErrorExit
        }
    }

    fun clear() {
        _pageIndex.tryEmit(0)
        _score.tryEmit(emptyMap())
    }
}