package com.stopsmoke.kekkek.presentation.rankingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingListViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _topUserList = MutableStateFlow<List<RankingListItem>>(emptyList())
    val topUserList get() = _topUserList.asStateFlow()

    fun getAllUserData() = viewModelScope.launch {
        val list = userRepository.getAllUserData().map{user->
            (user as User.Registered).toRankingListItem()
        }.filter { item->
            item.startTime != null
        }.sortedBy {item ->
            item.startTime!!
        }.take(30)
        _topUserList.emit(list)
    }
}
