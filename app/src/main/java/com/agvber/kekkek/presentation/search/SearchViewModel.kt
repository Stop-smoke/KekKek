package com.agvber.kekkek.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agvber.kekkek.common.asResult
import com.agvber.kekkek.core.domain.repository.SearchRepository
import com.agvber.kekkek.core.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    searchRepository: SearchRepository,
    userRepository: UserRepository,
) : ViewModel() {

    val user = userRepository.getUserData()

    val recommendedKeyword = searchRepository.getRecommendedKeyword()

    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

    fun updateKeyword(keyword: String) {
        viewModelScope.launch {
            _keyword.emit(keyword)
        }
    }

    private val _selectedKeyword = MutableSharedFlow<String>()
    val selectedKeyword = _selectedKeyword.asSharedFlow()

    fun updateSelectedKeyword(keyword: String) {
        viewModelScope.launch {
            _selectedKeyword.emit(keyword)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val post =
        keyword.debounce(500).flatMapLatest {
            if (it.isBlank()) {
                return@flatMapLatest emptyFlow()
            }

            searchRepository.searchPost(it)
        }.asResult()
}