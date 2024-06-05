package com.stopsmoke.kekkek.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchRepository: SearchRepository
) : ViewModel() {

    val recommendedKeyword = searchRepository.getRecommendedKeyword().getOrNull()

    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

    fun updateKeyword(keyword: String) = viewModelScope.launch {
        _keyword.emit(keyword)
    }
}