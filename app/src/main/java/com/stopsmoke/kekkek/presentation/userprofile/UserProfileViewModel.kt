package com.stopsmoke.kekkek.presentation.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.Achievement
import com.stopsmoke.kekkek.core.domain.model.Activities
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.CommentFilter
import com.stopsmoke.kekkek.core.domain.model.DatabaseCategory
import com.stopsmoke.kekkek.core.domain.model.Post
import com.stopsmoke.kekkek.core.domain.model.User
import com.stopsmoke.kekkek.core.domain.model.emptyActivities
import com.stopsmoke.kekkek.core.domain.repository.AchievementRepository
import com.stopsmoke.kekkek.core.domain.repository.CommentRepository
import com.stopsmoke.kekkek.core.domain.repository.PostRepository
import com.stopsmoke.kekkek.core.domain.repository.UserRepository
import com.stopsmoke.kekkek.presentation.getTotalDay
import com.stopsmoke.kekkek.presentation.my.achievement.AchievementItem
import com.stopsmoke.kekkek.presentation.my.achievement.CurrentProgress
import com.stopsmoke.kekkek.presentation.my.achievement.emptyCurrentProgress
import com.stopsmoke.kekkek.presentation.ranking.toRankingListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    postRepository: PostRepository,
    commentRepository: CommentRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    private val _uid = MutableStateFlow<String?>(null)
    private val uid get() = _uid

    fun updateUid(uid: String) {
        viewModelScope.launch {
            _uid.emit(uid)
        }
    }

    private val _errorHandler = MutableSharedFlow<Unit>()
    val errorHandler get() = _errorHandler.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val user: StateFlow<User.Registered?> = uid.flatMapLatest { userId ->
        if (userId == null) {
            return@flatMapLatest emptyFlow()
        }

        userRepository.getUserData(uid = userId)
            .let {
                when (it) {
                    is Result.Error -> {
                        viewModelScope.launch {
                            _errorHandler.emit(Unit)
                        }
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data
                }
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: Flow<PagingData<Post>> = uid.flatMapLatest { uid ->
        if (uid == null) {
            return@flatMapLatest emptyFlow()
        }

        postRepository.getPost(uid)
    }
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val myCommentHistory: Flow<PagingData<Comment>> = uid.flatMapLatest { uid ->
        if (uid == null) {
            return@flatMapLatest emptyFlow()
        }
        commentRepository.getCommentItems(CommentFilter.User(uid))
    }
        .catch {
            it.printStackTrace()
        }
        .cachedIn(viewModelScope)

    private val _postDetailScreenNavigate = MutableSharedFlow<String>()
    val postDetailScreenNavigate = _postDetailScreenNavigate.asSharedFlow()

    fun navigatePostDetailScreen(postId: String) {
        viewModelScope.launch {
            _postDetailScreenNavigate.emit(postId)
        }
    }


    //achievement
    private val _currentProgressItem = MutableStateFlow<CurrentProgress>(emptyCurrentProgress())
    val currentProgressItem: StateFlow<CurrentProgress> = _currentProgressItem.asStateFlow()

    val achievements = currentProgressItem.flatMapLatest { progress ->
        achievementRepository.getAchievementItems()
            .let {
                when (it) {
                    is Result.Error -> {
                        it.exception?.printStackTrace()
                        emptyFlow()
                    }

                    is Result.Loading -> emptyFlow()
                    is Result.Success -> it.data.map { pagingData ->
                        pagingData.map {
                            it.getItem().copy(
                                currentProgress = when (it.category) {
                                    DatabaseCategory.COMMENT -> progress.comment.toInt()
                                    DatabaseCategory.POST -> progress.post.toInt()
                                    DatabaseCategory.USER -> progress.user.toInt()
                                    DatabaseCategory.ACHIEVEMENT -> progress.achievement.toInt()
                                    DatabaseCategory.RANK -> progress.rank.toInt()
                                    else -> {
                                        0
                                    }
                                }
                            )
                        }
                    }
                }
            }
    }

    private fun Achievement.getItem() = AchievementItem(
        id = id,
        name = name,
        description = description,
        image = image,
        category = category,
        maxProgress = maxProgress,
        requestCode = requestCode
    )

    val activities: StateFlow<Activities> = uid.flatMapLatest { uid ->
        try {
            userRepository.getActivities(uid!!)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyFlow()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyActivities()
    )

    suspend fun getCurrentProgress(activities: Activities) {
        when (val userData = user.value) {
            is User.Registered -> {
                val list = userRepository.getAllUserData().map{user->
                    (user as User.Registered).toRankingListItem()
                }.filter { item->
                    item.startTime != null
                }.sortedBy {item ->
                    item.startTime!!
                }.map {
                    it.userID
                }
                val userRank = list.indexOf(userData.uid)+1

                _currentProgressItem.value = CurrentProgress(
                    user = userData.getTotalDay(),
                    comment = activities.commentCount,
                    post = activities.postCount,
                    rank = userRank.toLong(),
                    achievement = userData.clearAchievementsList.size.toLong()
                )
            }

            else -> _currentProgressItem.value = emptyCurrentProgress()
        }
    }
}