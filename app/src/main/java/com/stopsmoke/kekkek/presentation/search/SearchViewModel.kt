package com.stopsmoke.kekkek.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.stopsmoke.kekkek.algolia.AlgoliaDataSource
import com.stopsmoke.kekkek.algolia.model.SearchPostEntity
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.repository.SearchRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
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
    val post: Flow<PagingData<Post>> =
        keyword.debounce(500).flatMapLatest {
            if (it.isBlank()) {
                return@flatMapLatest emptyFlow()
            }

            searchRepository.searchPost(it)
        }
}